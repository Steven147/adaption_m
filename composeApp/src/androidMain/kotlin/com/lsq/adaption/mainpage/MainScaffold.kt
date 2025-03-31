package com.lsq.adaption.mainpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.mockscreen.MockPage
import kotlinx.coroutines.CoroutineScope

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScaffold(
    screenSettingsState: MutableState<ScreenSettings>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    scrollBehavior: TopAppBarScrollBehavior
) {
    Scaffold(
        topBar = {
            MainTopAppBar(scope, drawerState, scrollBehavior)
        },
//        bottomBar = { BottomTabBarView() },
    ) { paddingValue ->
        // 设置Scaffold的背景图片
//        AdaptionImageView {
////            Text(text = "hi")
//        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            contentAlignment = Alignment.Center
        ) {
            MockPage(screenSettingsState)
        }
//        TopTabBarView(paddingValue)
    }
}