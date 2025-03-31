package com.lsq.adaption.maindrawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.ui.PagerTabView
import kotlinx.coroutines.CoroutineScope

//val bottomSheetTabs = listOf("Params", "Detail")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ControllerBottomSheet(
    screenSettingsState: MutableState<ScreenSettings>,
    scope: CoroutineScope,
) {
    val selectedTab = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(selectedTab.intValue, 0F) { bottomSheetTabs.size }
    Box {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            when (bottomSheetTabs[it]) {
                "Params" -> {
                    ParamsPage(screenSettingsState, scope)
                }
                "Detail" -> {
                    DetailPage(screenSettingsState, scope)
                }
            }
        }
        PagerTabView(
            paddingValue = PaddingValues(),
            screenSettingsState = screenSettingsState,
            selectedTab = selectedTab,
            pagerState = pagerState,
            coroutineScope = scope,
            tabNameList = bottomSheetTabs,
            tabHeight = 48.dp,
        )
    }
}

@Composable
fun DetailPage(
    screenSettingsState: MutableState<ScreenSettings>,
    scope: CoroutineScope,
) {
}

@Composable
fun ParamsPage(
    screenSettingsState: MutableState<ScreenSettings>,
    scope: CoroutineScope,
) {
//    ParamsControllerContent(screenSettingsState)
}
