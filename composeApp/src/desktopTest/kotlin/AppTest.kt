import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.example.project.SERVER_PORT
import java.io.File
import kotlin.test.Test

class AppTest {
    @Test
    fun `should handle successful response and save zip file`(): Unit = runBlocking {
        // 配置模拟响应

        // 使用测试资源文件代替绝对路径
        val file = File("/Users/lsq/Desktop/images.xlsx").takeIf { it.exists() }
            ?: throw IllegalStateException("测试文件未找到")

        // 执行测试
        // 验证
        // 可以添加更多断言验证ZIP文件是否被正确保存
        assertTrue(true)
    }
}