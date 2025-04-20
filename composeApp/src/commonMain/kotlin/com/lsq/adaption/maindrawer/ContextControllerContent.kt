package com.lsq.adaption.maindrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.lsq.adaption.ScreenSettings
import com.ss.android.ugc.aweme.JsonItem
import com.ss.android.ugc.aweme.JsonItemView
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil.getMockAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.module
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


val localJson = Json {
    prettyPrint = true
    serializersModule = module
}

@Composable
fun ContextControllerContent(screenSettingsState: MutableState<ScreenSettings>) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
    ){
        TextField(
            value = try {
                localJson.encodeToString(screenSettingsState.value.adaptionContext)
            } catch (t: Throwable) {
                println(t.toString())
                ""
            },
            onValueChange = {
                screenSettingsState.value = screenSettingsState.value.copy(
                    adaptionContext = try {
                        localJson.decodeFromString<VideoAdaptionManagerContext>(it)
                    } catch (t: Throwable) {
                        println(t.toString())
                        getMockAdaptionManagerContext()
                    }
                )
            }
        )

        ScreenRatioUnit(screenSettingsState)


        JsonItemView(
            item = JsonItem(
                key = "context",
                value =  try {
                    localJson.encodeToJsonElement(screenSettingsState.value.adaptionContext)
                } catch (t: Throwable) {
                    println(t.toString())
                    null
                }
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
                value = localJson.encodeToJsonElement(screenSettingsState.value.adaptionResult)
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