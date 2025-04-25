import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.lsq.adaption.ScreenSettings
import com.ss.android.ugc.aweme.adaptionmonitor.DataJsonDelegate
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionData
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import kotlin.test.Test
import org.reflections.Reflections
import kotlin.reflect.cast

class TestDelegate {

//    @Test
    fun testJson() {
        // 创建 Reflections 实例，扫描指定包
        val reflections = Reflections("com.ss")

        // 获取 MyInterface 的所有实现类
        val subTypes = reflections.getSubTypesOf(IAdaptionData::class.java)
        // 筛选出具体的类
        val concreteClasses = subTypes.filter {!it.isInterface }

        println("$concreteClasses")

        // 存储创建的实例
        val instances = mutableListOf<IAdaptionData>()

        // 遍历实现类并创建实例
        for (clazz in concreteClasses) {
            try {
                // 使用反射创建实例
                val instance = clazz.getDeclaredConstructor().newInstance() as IAdaptionData
                instances.add(instance)
                println("instances.add, $clazz")
            } catch (e: NoSuchMethodException) {
                println("java.lang.NoSuchMethodException, $clazz")
            } catch (t: Throwable) {
                println("Exception ${t.javaClass.simpleName}, $clazz")
            }
        }
        // 调用实例的方法
        for (instance in instances) {
            try {
                DataJsonDelegate.apply { isTest = true }.encodeToString(instance)
                println("encodeToString(instance), ${instance.javaClass.simpleName}")
            } catch (e: ExceptionInInitializerError) {
                println("java.lang.ExceptionInInitializerError, ${instance.javaClass.simpleName}")
            } catch (t: Throwable) {
                println("Exception ${t.javaClass.simpleName}, ${instance.javaClass.simpleName}")
            }
        }
    }

    @Test
    fun testDataJsonDelegate() {
        val settings: MutableState<ScreenSettings> = mutableStateOf(ScreenSettings()).apply {
            // todo only check with one adaption situation, use more input value
            AdaptionMockDataUtil.getAdaptionResult(this, this.value.videoRatio)
        }
        DataJsonDelegate.isTest = true
        DataJsonDelegate.encodeToString(settings.value.adaptionResult)
        DataJsonDelegate.encodeToString(settings.value.adaptionParams)
        DataJsonDelegate.encodeToString(settings.value.adaptionContext)
    }

    // todo test params input invalid
}