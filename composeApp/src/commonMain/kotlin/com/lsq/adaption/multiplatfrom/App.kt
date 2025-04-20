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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.maindrawer.DrawerPager
import com.lsq.adaption.mockscreen.MockPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    MaterialTheme {
        val screenSettingsState = remember { mutableStateOf(ScreenSettings()) }
//        val viewModel: MainViewModel = viewModel { MainViewModel() }
//        val navController: NavHostController = rememberNavController()
//        val backStackEntry by navController.currentBackStackEntryAsState()
//        val currentScreen = AdaptionScreen.valueOf(
//            backStackEntry?.destination?.route ?: AdaptionScreen.First.name
//        )

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
//                bottomBar = {
//                    BottomAppBar {
//                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//                            for (screen in AdaptionScreen.entries) {
//                                IconButton(onClick = { navController.navigate(screen.name) }) {
//                                    Icon(
//                                        painter = painterResource(Res.drawable.compose_multiplatform),
//                                        contentDescription = screen.visualName,
//                                        tint = if (currentScreen == screen) Color.Red else Color.Gray,
//                                        modifier = Modifier.size(24.dp)
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
            ) { paddingValues->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    MockPage(screenSettingsState)
                }
//                NavHost(
//                    navController = navController,
//                    startDestination = AdaptionScreen.First.name
//                ) {
//
//                    composable(AdaptionScreen.First.name) {
//                        var showContent by remember { mutableStateOf(false) }
//                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                            Button(onClick = { showContent = !showContent }) {
//                                Text("CLICK ME!")
//                            }
//                            AnimatedVisibility(showContent) {
//                                val greeting = remember { Greeting().greet() }
//                                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                                    Text("Compose: $greeting")
//                                }
//                            }
//                        }
//                    }
//                    composable(AdaptionScreen.Second.name) {
//                    }
//                    composable(AdaptionScreen.Third.name) {
//
//                    }
//                    composable(AdaptionScreen.Fourth.name) {
//
//                    }
//                    composable(AdaptionScreen.Fifth.name) {
//
//                    }
//                }
            }
        }
    }
}

//enum class AdaptionScreen(val visualName: String) {
//    First("first"),
//    Second("second"),
//    Third("third"),
//    Fourth("fourth"),
//    Fifth("fifth");
//}
//
////enum class AdaptionFeedScreen(val visualName: String) {
////    First("for you"),
////    Second("follow"),
////}
////
//
//class MainViewModel : ViewModel() {
//    private val _uiState = MutableStateFlow(MainUiState(AdaptionScreen.First))
//    val uiState = _uiState.asStateFlow()
//}
//
//data class MainUiState(
//    val currentScreen: AdaptionScreen
//)