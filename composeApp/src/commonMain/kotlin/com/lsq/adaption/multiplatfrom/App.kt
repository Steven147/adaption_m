package com.lsq.adaption.multiplatfrom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import adaption_m.composeapp.generated.resources.Res
import adaption_m.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.maindrawer.DrawerPager
import com.lsq.adaption.mockscreen.MockPage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.UrlsMapResult
import org.example.project.UrlsResult
import org.example.project.getBytes
import org.example.project.getUrlsMapResult
import org.example.project.getUrlsResult
import org.example.project.openFileSaver


@Composable
@Preview
fun App() {
    MaterialTheme {
        val screenSettingsState = remember { mutableStateOf(ScreenSettings()) }
//        val viewModel: MainViewModel = viewModel { MainViewModel() }
        val navController: NavHostController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = AdaptionScreen.valueOf(
            backStackEntry?.destination?.route ?: AdaptionScreen.First.name
        )

        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        )

        val scope = rememberCoroutineScope()

        // use drawer wrap scaffold
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                // todo drawer pager to scroller view
                DrawerPager(
                    screenSettingsState = screenSettingsState,
                    scope = scope
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Adaption Example [${getPlatform().name}]",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (sheetState.isVisible.not()) {
                                        sheetState.show()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                    )
                },
                bottomBar = {
                    BottomAppBar {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            for (screen in AdaptionScreen.entries) {
                                IconButton(onClick = { navController.navigate(screen.name) }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.compose_multiplatform),
                                        contentDescription = screen.visualName,
                                        tint = if (currentScreen == screen) Color.Red else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            ) { paddingValues->
                NavHost(
                    navController = navController,
                    startDestination = AdaptionScreen.First.name
                ) {

                    composable(AdaptionScreen.First.name) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            MockPage(screenSettingsState)
                        }
                    }
                    composable(AdaptionScreen.Second.name) {
                        FileUploadPage(scope)
                    }
                    composable(AdaptionScreen.Third.name) {
                        var showContent by remember { mutableStateOf(false) }
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(onClick = { showContent = !showContent }) {
                                Text("CLICK ME!")
                            }
                            AnimatedVisibility(showContent) {
                                val greeting = remember { Greeting().greet() }
                                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                                    Text("Compose: $greeting")
                                }
                            }
                        }
                    }
                    composable(AdaptionScreen.Fourth.name) {

                    }
                    composable(AdaptionScreen.Fifth.name) {

                    }
                }
            }
        }
    }
}
enum class AdaptionScreen(val visualName: String) {
    First("first"),
    Second("second"),
    Third("third"),
    Fourth("fourth"),
    Fifth("fifth");
}

//enum class AdaptionFeedScreen(val visualName: String) {
//    First("for you"),
//    Second("follow"),
//}
//
// 新增状态密封类
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState(AdaptionScreen.First))
    val uiState = _uiState.asStateFlow()
    private val _uploadState = MutableStateFlow<UiState<UrlsResult>>(UiState.Idle)
    private val _crawlProgress = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _crawlState = MutableStateFlow<UiState.Success<UrlsMapResult>>(UiState.Success(UrlsMapResult(emptyMap())))
    val crawlProgress = _crawlProgress.asStateFlow()
    val crawlState = _crawlState.asStateFlow()


    private val _downloadState = MutableStateFlow<UiState<ByteArray>>(UiState.Idle)
    val uploadState = _uploadState.asStateFlow()
    val downloadState = _downloadState.asStateFlow()

    fun getUploadResult() = (uploadState.value as? UiState.Success<UrlsResult>)?.data

    fun getCrawlResult() = (crawlState.value as? UiState.Success<UrlsMapResult>)?.data

    fun getDownloadResult() = (downloadState.value as? UiState.Success<ByteArray>)?.data

    fun uploadExcel(bytes: ByteArray) {
        viewModelScope.launch {
            _uploadState.value = UiState.Loading
            try {
                val result = getUrlsResult(bytes)
                if (result.urls.isEmpty()) throw Exception("空结果")
                _uploadState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uploadState.value = UiState.Error(e.message ?: "上传失败")
            }
        }
    }

    fun startCrawling(url: String) {
        viewModelScope.launch {
            _crawlProgress.value += (url to true)
            try {
                val result = getUrlsMapResult(UrlsResult(listOf(url))).urlsMap
                val oldMap = getCrawlResult()?.urlsMap ?: emptyMap()
                _crawlState.value = UiState.Success(UrlsMapResult(oldMap + result))
            } finally {
                _crawlProgress.value -= url
            }
        }
    }

    fun startDownloading() {
        viewModelScope.launch {
            _downloadState.value = UiState.Loading
            try {
                val urls = (this@MainViewModel.crawlState.value as? UiState.Success<UrlsMapResult>)?.data?.toValues() ?: throw Exception("非法上一步结果")
                val result = getBytes(urls)
                _downloadState.value = UiState.Success(result)
            } catch (e: Exception) {
                _downloadState.value = UiState.Error(e.message?: "下载失败")
            }
        }
    }
}

data class MainUiState(
    val currentScreen: AdaptionScreen
)

// 简化后的 UI 组件
@Composable
fun FileUploadPage(scope: CoroutineScope) {
    val viewModel: MainViewModel = viewModel { MainViewModel() }
    var currentStep by remember { mutableIntStateOf(0) }
    // 步骤标题
    val stepTitles = listOf(
        "1. 上传Excel文件",
        "2. 执行爬取任务",
        "3. 下载结果文件"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        StepProgress(currentStep, stepTitles)

        when (currentStep) {
            0 -> UploadStep(
                scope = scope,
                viewModel = viewModel,
                onSuccess = { currentStep++ }
            )

            1 -> CrawlStep(
                viewModel = viewModel,
                onSuccess = { currentStep++ }
            )

            2 -> DownloadStep(
                scope = scope,
                viewModel = viewModel,
                onSuccess = { currentStep++ }
            )
        }
    }
}



@Composable
private fun StepProgress(currentStep: Int, titles: List<String>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        titles.forEachIndexed { index, title ->
            val color = if (index <= currentStep) Color.Blue else Color.Gray
            val textColor = if (index <= currentStep) Color.White else Color.DarkGray

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .background(color, shape = MaterialTheme.shapes.small)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ... 保持 FileUploadPage 不变 ...

@Composable
private fun UploadStep(
    scope: CoroutineScope,
    viewModel: MainViewModel,
    onSuccess: () -> Unit
) {
    val uploadState by viewModel.uploadState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (uploadState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
                Text("正在上传文件...")
            }
            is UiState.Success -> {
                LaunchedEffect(Unit) { onSuccess() }
                Text("✓ 上传成功", color = Color.Green)
            }
            is UiState.Error -> {
                Text("错误: ${(uploadState as UiState.Error).message}", color = Color.Red)
                Button({ viewModel.uploadExcel(byteArrayOf()) }) {
                    Text("重试上传")
                }
            }
            UiState.Idle -> {
                val filePicker = rememberFilePickerLauncher(
                    type = FileKitType.File(listOf("xlsx")),
                    onResult = { file ->
                        scope.launch {
                            file?.readBytes()?.let { bytes ->
                                viewModel.uploadExcel(bytes)
                            }
                        }
                    }
                )
                Button({ filePicker.launch() }) {
                    Text("选择Excel文件")
                }
            }
        }
    }
}


// 更新 UI 组件
@Composable
fun CrawlStep(
    viewModel: MainViewModel,
    onSuccess: () -> Unit
) {
    val progress by viewModel.crawlProgress.collectAsState()
    val urlsMap by viewModel.crawlState.collectAsState()
    val urls = viewModel.getUploadResult()?.urls
    Column {

        // 显示完成进度
        Text("已完成 ${urlsMap?.data?.urlsMap?.size ?: 0}/${viewModel.getUploadResult()?.urls?.size ?: -1} 项")

        // 当有任意结果时显示下载按钮
        if (viewModel.crawlState.value?.data?.urlsMap?.isNotEmpty() == true) {
            Button(onClick = onSuccess) {
                Text("进入下载步骤")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text("检测到${urls?.size ?: 0}个URL", modifier = Modifier.padding(8.dp))
            }

            items(urls?.size ?: 0) { index ->
                val url = urls?.get(index) ?: return@items
                val isLoading = progress.getOrElse(url) { false }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.startCrawling(url)
                        },
                        enabled = !isLoading
                    ) {
                        Text(url)
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp)
                        )
                    } else {
                        Text(
                            text = "image url: ${urlsMap.data.urlsMap.getOrElse(url) { "" }}",
                            color = Color.Green,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DownloadStep(
    scope: CoroutineScope,
    viewModel: MainViewModel,
    onSuccess: () -> Unit
) {
    val downloadState by viewModel.downloadState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (downloadState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
                Text("正在生成下载文件...")
            }
            is UiState.Success -> {
                LaunchedEffect(Unit) { onSuccess() }
                Text("✓ 下载完成", color = Color.Green)
            }
            is UiState.Error -> {
                Text("错误: ${(downloadState as UiState.Error).message}", color = Color.Red)
                Button({ viewModel.startDownloading() }) {
                    Text("重试下载")
                }
            }
            UiState.Idle -> {
                Button({ viewModel.startDownloading() }) {
                    Text("下载结果文件")
                }
                Text("下载完成后可在文件管理器中查看", style = MaterialTheme.typography.caption)
            }
        }
    }
}


//
//@Composable
//private fun UploadStep(
//    scope: CoroutineScope,
//    onUploadStart: () -> Unit,
//    onUploadSuccess: (UrlsResult) -> Unit,
//    onUploadError: (String) -> Unit
//) {
//    val filePicker = rememberFilePickerLauncher(
//        type = FileKitType.File(listOf("xlsx")),
//        onResult = { excelFile ->
//            excelFile ?: return@rememberFilePickerLauncher
//            scope.launch {
//                try {
//                    onUploadStart()
//                    uploadExcelAndGetUrls(excelFile.readBytes()).getResultSafely<UrlsResult>()?.let(onUploadSuccess)
//                } catch (e: Exception) {
//                    onUploadError("文件上传失败: ${e.message}")
//                }
//            }
//        }
//    )
//
//    Button(
//        onClick = { filePicker.launch() },
//        modifier = Modifier.padding(8.dp)
//    ) {
//        Text("选择Excel文件")
//    }
//}
//
//@Composable
//private fun CrawlStep(
//    scope: CoroutineScope,
//    urlsResult: UrlsResult?,
//    onCrawlStart: () -> Unit,
//    onCrawlSuccess: (UrlsMapResult) -> Unit,
//    onCrawlError: (String) -> Unit
//) {
//    Column {
//        Text("检测到${urlsResult?.urls?.size ?: 0}个URL", modifier = Modifier.padding(8.dp))
//
//        Button(
//            onClick = {
//                scope.launch {
//                    try {
//                        onCrawlStart()
//                        urlsResult?: return@launch
//                        startCrawling(urlsResult).getResultSafely<UrlsMapResult>()?.let(onCrawlSuccess)
//                    } catch (e: Exception) {
//                        onCrawlError("爬取失败: ${e.message}")
//                    }
//                }
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text("开始执行爬取")
//        }
//    }
//}
//
//@Composable
//private fun DownloadStep(
//    scope: CoroutineScope,
//    urlsResult: UrlsResult?,
//    onDownload: () -> Unit
//) {
//    Column {
//        Button(
//            onClick = {
//                scope.launch {
//                    urlsResult?.let {
//                        downloadZipFile(it).getResultSafely<ByteArray>()?.let {
//                            openFileSaver(it, "result.zip")
//                        }
//                    }
//                    onDownload()
//                }
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text("下载结果文件")
//        }
//
//        Text("下载完成后可在文件管理器中查看",
//            style = MaterialTheme.typography.caption,
//            modifier = Modifier.padding(8.dp))
//    }
//}
