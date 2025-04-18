package com.lsq.adaption.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ScreenSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTabView(
    paddingValue: PaddingValues,
    screenSettingsState: MutableState<ScreenSettings>,
    selectedTab: MutableIntState,
    pagerState: PagerState,
    tabNameList: List<String>,
    tabHeight: Dp,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValue)
            .alpha(0.5f)

    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab.intValue,
            edgePadding = 0.dp, // 设置边缘填充为 0，可根据需要调整
            modifier = Modifier
                .height(tabHeight)
                .align(Alignment.CenterHorizontally)
        ) {
            tabNameList.forEachIndexed { index, text ->
                Tab(
                    modifier = Modifier
                        .width(40.dp)
                        .height(tabHeight),
                    text = { Text(text) },
                    selected = selectedTab.intValue == index,
                    onClick = {
                        // 使用coroutineScope来调用scrollToPage
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}
