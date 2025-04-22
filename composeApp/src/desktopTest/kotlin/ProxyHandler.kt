import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

fun main() {
    val joe = object: PersonBean {
        var n = "joe"
        var r = 0
        override fun getName(): String { return n }
        override fun setName(name: String) { n = name }
        override fun getRating(): Int { return r }
        override fun setRating(rating: Int) { r = rating }
    }

    val ownerProxy = getProxyBy(joe, OwnerInvocationHandler(joe))
    println("Name is ${ownerProxy.getName()}")
    ownerProxy.setName("Joe")
    println("New Name is ${ownerProxy.getName()}")
    try {
        ownerProxy.setRating(10)
    } catch (e: Exception) {
        println("can not set rating by ownerProxy")
    }

    val nonOwnerProxy = getProxyBy(joe, NonOwnerInvocationHandler(joe))
    println("Name is ${ownerProxy.getName()}")
    try {
        nonOwnerProxy.setName("Joe")
    } catch (e: Exception) {
        println("can not set name by nonOwnerProxy")
    }
    nonOwnerProxy.setRating(10)
    println("New rating is ${nonOwnerProxy.getRating()}")


}

interface PersonBean {
    fun getName(): String
    fun setName(name: String) // only owner can call

    fun getRating(): Int
    fun setRating(rating: Int) // only non owner can call
}


fun getProxyBy(person: PersonBean, handler: InvocationHandler): PersonBean {
    return Proxy.newProxyInstance(
        person.javaClass.classLoader,
        person.javaClass.interfaces,
        handler
    ) as PersonBean
}


class OwnerInvocationHandler(
    private val person: PersonBean
): InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        method ?: throw IllegalArgumentException()
        if (method.name.startsWith("get"))
            return method.invoke(person, *args.orEmpty())
        if (method.name.equals("setRating"))
            throw IllegalArgumentException()
        if (method.name.startsWith("set"))
            return method.invoke(person, *args.orEmpty())
        return null
    }

}

class NonOwnerInvocationHandler(
    private val person: PersonBean
): InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        method ?: throw IllegalArgumentException()
        if (method.name.startsWith("get"))
            return method.invoke(person, *args.orEmpty())
        if (method.name.equals("setRating"))
            return method.invoke(person, *args.orEmpty())
        if (method.name.startsWith("set"))
            throw IllegalArgumentException()
        return null
    }

}