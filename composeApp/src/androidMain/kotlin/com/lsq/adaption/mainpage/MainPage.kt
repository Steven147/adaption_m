package com.lsq.adaption.mainpage

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.lsq.adaption.ScreenSettings
import com.lsq.adaption.maindrawer.MainDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = false)
@Composable
fun MainPage() {

    val screenSettingsState = remember { mutableStateOf(ScreenSettings()) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MainDrawer(
        screenSettingsState = screenSettingsState,
        drawerState = drawerState,
        scope = scope
    ) {
        MainScaffold(screenSettingsState, scope, drawerState, scrollBehavior)
    }
}

