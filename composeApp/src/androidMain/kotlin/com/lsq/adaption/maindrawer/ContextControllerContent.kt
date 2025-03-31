package com.lsq.adaption.maindrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.google.gson.GsonBuilder
import com.lsq.adaption.ScreenSettings
import com.ss.android.ugc.aweme.JsonItem
import com.ss.android.ugc.aweme.JsonItemView
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

val gson by lazy {
    GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .disableHtmlEscaping()
        .create()
}

@Composable
fun ContextControllerContent(screenSettingsState: MutableState<ScreenSettings>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ){
        InputTextField(
            text = remember { mutableStateOf(
                gson.toJson(screenSettingsState.value.adaptionContext)
//                Json.encodeToString(screenSettingsState.value.adaptionContext)
            ) },
            end = {
                screenSettingsState.value = screenSettingsState.value.copy(
                    adaptionContext =
                        gson.fromJson(it, VideoAdaptionManagerContext::class.java)
//                        Json.decodeFromString<MockAdaptionContext>(it)
                )
            }
        )

        ScreenRatioUnit(screenSettingsState)

        JsonItemView(
            item = JsonItem(
                key = "context",
                value = JSONObject(
                    gson.toJson(
                        screenSettingsState.value.adaptionContext
                    )
                )
            )
        )
    }
}


@Composable
fun ResultControllerContent(screenSettingsState: MutableState<ScreenSettings>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ){
        JsonItemView(
            item = JsonItem(
                key = "result",
                value = JSONObject(
                    gson.toJson(
                        screenSettingsState.value.adaptionResult
                    )
                )
            )
        )
    }
}


@Composable
private fun InputTextField(
    text: MutableState<String>,
    end: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // 输入框组件
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("input") },
        // 设置键盘类型
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        // 设置键盘动作
        keyboardActions = KeyboardActions(
            onDone = {
                end(text.value)
                keyboardController?.hide()
            },
            onGo = {
                end(text.value)
                keyboardController?.hide()
            },
        ),
    )
}