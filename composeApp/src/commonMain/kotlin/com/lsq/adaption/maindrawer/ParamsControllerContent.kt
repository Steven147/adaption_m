package com.lsq.adaption.maindrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.lsq.adaption.ScreenSettings
import com.ss.android.ugc.aweme.JsonItem
import com.ss.android.ugc.aweme.JsonItemView
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionDisplayUtil.getScaleBy9
import com.ss.android.ugc.aweme.videoadaption.BaseAdaptionStrategyFactory
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.getBottomLineName
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.getTopLineName
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.toCount
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.toType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement


@Composable
fun ParamsControllerContent(screenSettingsState: MutableState<ScreenSettings>) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(horizontal = 8.dp).fillMaxSize().verticalScroll(scrollState)) {
//        Text("Params", modifier = Modifier.padding(16.dp))
//
//        HorizontalDivider()
        val jsonElement = localJson.encodeToJsonElement(screenSettingsState.value.adaptionParams)
        JsonItemView(
            item = JsonItem(
                key = "params",
                value = jsonElement
            )
        )

        Divider()


        // change unit
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use mock video:")
            Switch(
                checked = screenSettingsState.value.mockVideoRatioEnable,
                onCheckedChange = { checked ->
                    screenSettingsState.value = screenSettingsState.value.copy(
                        mockVideoRatioEnable = checked
                    )
                })
        }
        VideoRatioUnit(screenSettingsState)

        Divider()
        // todo padding not effect
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use mock padding: ")
            Switch(
                checked = screenSettingsState.value.mockPaddingEnable,
                onCheckedChange = { checked ->
                    screenSettingsState.value = screenSettingsState.value.copy(
                        mockPaddingEnable = checked
                    )
                })
        }

        // top padding change unit
        SliderUnit(screenSettingsState, true)
        // bottom padding change unit
        SliderUnit(screenSettingsState, false)


        Divider()

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use mock scale: ")
            Switch(
                checked = screenSettingsState.value.mockScaleEnable,
                onCheckedChange = { checked ->
                    screenSettingsState.value = screenSettingsState.value.copy(
                        mockScaleEnable = checked
                    )
                })
        }

        // scaleMode change unit
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("      Fit <-")
            Switch(
                checked = screenSettingsState.value.mockScaleMode == ContentScale.Crop,
                onCheckedChange = { checked ->
                    screenSettingsState.value = screenSettingsState.value.copy(
                        mockScaleMode = if (checked) ContentScale.Crop else ContentScale.Fit
                    )
                })
            Text("-> Crop")
        }
    }
}

@Composable
fun ScreenRatioUnit(
    screenSettingsState: MutableState<ScreenSettings>,
) {
    val baseAdaptionStrategyFactory = screenSettingsState.value.adaptionContext.strategyFactory as? BaseAdaptionStrategyFactory ?: return
    OptionScaleSliderUnit(
        title = "Mock Screen ratio",
        minValue = 15f/9,
        maxValue = 24f/9,
        type = baseAdaptionStrategyFactory.feedScreenContext.let {
            it.screenHeight / it.screenWidth
        },
        typeToValue = {
            it
        },
        typeToName = {
            "[${it.getScaleBy9()}]"
        },
        onValueChange = {
            screenSettingsState.value = screenSettingsState.value.copy(
                adaptionContext = screenSettingsState.value.adaptionContext.copy(
                    strategyFactory = baseAdaptionStrategyFactory.copy(
                        feedScreenContext = baseAdaptionStrategyFactory.feedScreenContext.copy(
                            screenHeight = baseAdaptionStrategyFactory.feedScreenContext.screenWidth * it
                        )
                    )
                ),
            )
        }
    )
}

@Composable
fun VideoRatioUnit(
    screenSettingsState: MutableState<ScreenSettings>,
) {
    OptionScaleSliderUnit(
        title = "Mock Video ratio",
        minValue = 9f/24,
        maxValue = 24f/9,
        type = screenSettingsState.value.videoRatio,
        typeToValue = {
            it
        },
        typeToName = {
            "[${it.getScaleBy9()}]"
        },
        onValueChange = {
            screenSettingsState.value = screenSettingsState.value.copy(
                videoRatio = it
            )
        }
    )

}

@Composable
private fun SliderUnit(
    screenSettingsState: MutableState<ScreenSettings>,
    isTop: Boolean
) {
    OptionScaleSliderUnit(
        title = if (isTop) "top padding:" else "bottom padding:",
        minValue = 0f,
        maxValue = (if (isTop) ScreenArea.topAreaList.size else ScreenArea.bottomAreaList.size).toFloat(),
        optionCnt = (if (isTop) ScreenArea.topAreaList.size else ScreenArea.bottomAreaList.size) + 1,
        type = if (isTop) screenSettingsState.value.screenAdaptionTopType
        else screenSettingsState.value.screenAdaptionBottomType,
        typeToValue = { type ->
            type.toCount().toFloat()
        },
        typeToName = { type ->
            if (isTop) getTopLineName(type)
            else getBottomLineName(type)
        },
        onValueChange = {
            val newType = it.toInt().toType()
            screenSettingsState.value = if (isTop) screenSettingsState.value.copy(
                screenAdaptionTopType = newType
            ) else screenSettingsState.value.copy(
                screenAdaptionBottomType = newType
            )
        },
    )
}


@Composable
private fun <T: Any> OptionScaleSliderUnit(
    title: String,
    minValue: Float,
    maxValue: Float,
    type: T,
    typeToValue:(T)->Float,
    typeToName:(T)->String,
    onValueChange:(Float)->Unit,
    optionCnt: Int? = null, // step width 1.0
) {
    Text(text = title)
    Row {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            val innerSteps = if (optionCnt == null || optionCnt == 1) 0 else optionCnt - 2
            // 创建一个 Slider 实例
            Slider(
                value = typeToValue(type), // 初始值
                onValueChange = onValueChange,
                valueRange = minValue..maxValue, // 值的范围
                steps = innerSteps
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "type: ${type}, \nvalue: ${typeToValue(type)}, \nname: ${typeToName(type)},"
            )
        }
    }
}
