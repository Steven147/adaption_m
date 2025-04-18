package com.lsq.adaption.maindrawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.ui.PagerTabView
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.ui.tooling.preview.Preview

@Deprecated("")
@Composable
fun MainDrawer(
    screenSettingsState: MutableState<ScreenSettings>,
    drawerState: Any,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
}


val bottomSheetTabs = listOf("Context", "Params", "Result")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerPager(
    screenSettingsState: MutableState<ScreenSettings>,
    scope: CoroutineScope
    ) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(selectedTab.intValue, 0F) { bottomSheetTabs.size }
    Column {
        PagerTabView(
            paddingValue = PaddingValues(),
            screenSettingsState = screenSettingsState,
            selectedTab = selectedTab,
            pagerState = pagerState,
            coroutineScope = scope,
            tabNameList = bottomSheetTabs,
            tabHeight = 48.dp,
        )
        HorizontalPager(
            state = pagerState,
            Modifier.fillMaxSize()
        ) {
            when(it) {
                0 -> { ContextControllerContent(screenSettingsState) }
                1 -> { ParamsControllerContent(screenSettingsState) }
                2 -> { ResultControllerContent(screenSettingsState) }
            }


            LaunchedEffect(selectedTab) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    selectedTab.intValue = page
                }
            }
        }
    }
}
