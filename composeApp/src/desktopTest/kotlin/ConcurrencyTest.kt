import com.ss.android.ugc.aweme.adaptionmonitor.DataJsonDelegate
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.junit.runner.notification.RunListener.ThreadSafe
import kotlin.test.Test
import org.reflections.Reflections
import kotlin.system.measureTimeMillis

class ConcurrencyTest {


    @Test
    fun main() {
        val manager = VolatileTokenManager()
        val threadCount = 5
        val threads = mutableListOf<Thread>()

        for (i in 1..threadCount) {
            val thread = Thread {
                println("${Thread.currentThread().name} is starting.")
                val result = manager.updateToken()
                println("${Thread.currentThread().name} got result: $result")
                println("${Thread.currentThread().name} is finishing.")
            }
            thread.name = "Thread-$i"
            threads.add(thread)
            thread.start()
        }

        // 等待所有线程执行完毕
        threads.forEach { it.join() }
        println("All threads have finished.")
    }


    @Test
    fun main2() = runBlocking {
        val manager = CoroutineTokenManager()
        val coroutineCount = 5
        val jobs = mutableListOf<Job>()
        withContext(Dispatchers.Default) {
            repeat(coroutineCount) { index ->
                println("${Thread.currentThread().name} launching coroutine $index.")
                delay(10)
                val job = launch {
                    println("Coroutine-$index is starting.")
                    val result = manager.updateToken()
                    println("Coroutine-$index got result: $result")
                    println("Coroutine-$index is finishing.")
                }
                jobs.add(job)
            }
        }

        jobs.forEach { it.join() }
        println("All coroutines have finished.")
    }

    @Test
    fun main3() = runBlocking {
        withContext(Dispatchers.Default) {
            massiveRun {
                withContext(counterContext) {
                    counter++
                }
            }
        }
        println("Counter = ${counter}")
    }
    var counter = 0
    val counterContext = newSingleThreadContext("CounterContext")
    suspend fun massiveRun(action: suspend () -> Unit) {
        val n = 100  // number of coroutines to launch
        val k = 1000 // times an action is repeated by each coroutine
        val time = measureTimeMillis {
            coroutineScope { // scope for coroutines
                repeat(n) {
                    launch {
                        repeat(k) { action() }
                    }
                }
            }
        }
        println("Completed ${n * k} actions in $time ms")
    }

}


//编写一段代码，在页面启动时使用 getToken API（异步）来获取一次性的token，并进行全局管理，token的有效期是5mins，需要保持当前缓存实时可用。
//用户点击按钮后，如果token已经获取成功，则使用Token调用服务获取服务结果。
//如果token未完成获取，则等待token的获取结果后，再使用token获取服务结果。
//如果token获取失败，则认为服务调用失败
//如果服务调用成功或者失败后，需要重新获取Token。

open class TokenManager {
    protected var curToken: String? = null
    protected var tokenExpiredTime: Long? = null
    protected var isGettingToken: Boolean = false
    protected val lock = Object()

    open fun updateToken(): String {
        synchronized(lock) {
            while (isGettingToken) {
                println("${Thread.currentThread().name} is waiting for getting token")
                lock.wait()
                println("${Thread.currentThread().name} finish waiting")
            }
            println("${Thread.currentThread().name} into get token")
            // not getting new token
            if (curToken != null && tokenExpiredTime != null && tokenExpiredTime!! >= System.currentTimeMillis()) {
                println("${Thread.currentThread().name} get token ${curToken}.")
                return curToken!!
            }
            // no valid token, while getting token, other thread will wait
            isGettingToken = true
        }

        val finalToken = getToken()
        synchronized(lock) {
            curToken = finalToken
            tokenExpiredTime = System.currentTimeMillis() + 5 * 60 * 1000
            // finish getting token, notify other thread
            isGettingToken = false
            println("${Thread.currentThread().name} has updated the token ${finalToken}.")
            lock.notifyAll()
        }
        return finalToken
    }


    open fun getToken(): String {
        // sleep 100ms
        Thread.sleep(100)
        return "token:${System.currentTimeMillis()}"
    }
}

class VolatileTokenManager: TokenManager() {
    @ThreadSafe
    data class TokenInfo(val token: String, val expiredTime: Long)
    private var tokenInfo: TokenInfo? = null

    override fun updateToken(): String {
        val tokenInfoRead = tokenInfo
        if (tokenInfoRead!= null && tokenInfoRead.expiredTime >= System.currentTimeMillis()) {
            println("${Thread.currentThread().name} get token ${tokenInfoRead.token}.")
            return tokenInfoRead.token
        }

        val finalToken = getToken()
        tokenInfo = TokenInfo(finalToken, System.currentTimeMillis() + 5 * 60 * 1000)
        // finish getting token, notify other thread
        println("${Thread.currentThread().name} has updated the token ${tokenInfo?.token}.")
        return finalToken
    }
}


class CoroutineTokenManager {
    private var curToken: String? = null
    private var tokenExpiredTime: Long? = null
    private var isGettingToken: Boolean = false
    private val lock = Mutex()

    suspend fun updateToken(): String {
        lock.withLock {
            while (isGettingToken) {
                println("${Thread.currentThread().name} is waiting for getting token")
                yield()
                println("${Thread.currentThread().name} finish waiting")
            }
            println("${Thread.currentThread().name} into get token")
            // not getting new token
            if (curToken != null && tokenExpiredTime != null && tokenExpiredTime!! >= System.currentTimeMillis()) {
                println("${Thread.currentThread().name} get token ${curToken}.")
                return curToken!!
            }
            // no valid token, while getting token, other thread will wait
            isGettingToken = true
        }

        val finalToken = getToken()
        lock.withLock {
            curToken = finalToken
            tokenExpiredTime = System.currentTimeMillis() + 5 * 60 * 1000
            // finish getting token, notify other thread
            isGettingToken = false
            println("${Thread.currentThread().name} has updated the token ${finalToken}.")
        }
        return finalToken
    }

    suspend fun getToken(): String {
        delay(100)
        return "token:${System.currentTimeMillis()}"
    }
}
