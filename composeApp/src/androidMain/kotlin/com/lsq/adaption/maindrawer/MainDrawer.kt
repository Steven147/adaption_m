package com.lsq.adaption.maindrawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.ui.PagerTabView
import com.lsq.adaption.ui.theme.AdaptionTheme
import kotlinx.coroutines.CoroutineScope

@Composable
@Preview(showBackground = false)
fun MainDrawerPreview() {
    AdaptionTheme {
        MainDrawer(
            screenSettingsState = remember { mutableStateOf(ScreenSettings()) },
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            scope = rememberCoroutineScope()
        ) {

        }
    }
}

@Composable
fun MainDrawer(
    screenSettingsState: MutableState<ScreenSettings>,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(end = 16.dp)
            ) {
//                Text("Params", modifier = Modifier.padding(16.dp))
//                Divider()
//                NavigationDrawerItem(
//                    label = { Text(text = "Drawer Item") },
//                    selected = false,
//                    onClick = { /*TODO*/ }
//                )
                DrawerPager(screenSettingsState, scope)
            }
        },
        content = content
    )
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
