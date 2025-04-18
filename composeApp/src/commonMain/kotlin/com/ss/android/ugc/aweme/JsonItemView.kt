package com.ss.android.ugc.aweme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive


@Composable
fun PreviewJsonItemView() {
    val jsonObj = JsonObject(
        mapOf(
            "str" to JsonPrimitive("string"),
            "num" to JsonPrimitive(123),
            "bool" to JsonPrimitive(true),
            "null" to JsonPrimitive(null as String?),
            "array" to JsonArray(
                listOf(
                    JsonPrimitive("string"),
                    JsonPrimitive(123),
                    JsonPrimitive(true),
                    JsonPrimitive(null as String?),
                    JsonObject(
                        mapOf(
                            "str" to JsonPrimitive("string"),
                            "num" to JsonPrimitive(123),
                            "bool" to JsonPrimitive(true),
                            "null" to JsonPrimitive(null as String?)
                        )
                    )
                )
            ),
            "obj" to JsonObject(
                mapOf(
                    "str" to JsonPrimitive("string"),
                    "num" to JsonPrimitive(123),
                    "bool" to JsonPrimitive(true),
                    "null" to JsonPrimitive(null as String?)
                )
            )
        )
    )
    val jsonString = Json.encodeToString(jsonObj)
    Column {
        Text(text = jsonString)
        JsonItemView(
            item = JsonItem(
                key = "root",
                value = jsonObj
            )
        )
    }
}


@Composable
fun JsonItemView(
    item: JsonItem,
    initExpand: Boolean = true,
) {
    val state = remember {
        mutableStateOf(JsonItemState(isExpanded = initExpand))
    }
    val isExpanded = state.value.isExpanded
    Column {
        val type = item.valueType
        val color = type.getColor()
        val children = item.getChildItemList()
        val hasChild = children != null
        // start current view
        Row {
            // beginner view
            if (hasChild && isExpanded) {
                Text("- ", modifier = Modifier.clickable {
                    state.value = state.value.copy(
                        isExpanded = false
                    )
                })
            } else if (hasChild && !isExpanded) {
                Text("+ ", modifier = Modifier.clickable {
                    state.value = state.value.copy(
                        isExpanded = true
                    )
                })
            } else {
                Text("  ")
            }
            // key view, beside array list item, show key
            if (item.key != null) {
                Text(
                    text = "${item.key} : ",
                    color = Color.Black
                )
            } else {
                LaunchedEffect(key1 = null) {

                }
            }
            // value view
            Text(item.getCurrentValue(), color = color)
        }
        // child view
        if (hasChild && children != null && isExpanded) {
            for (child in children) {
                Row {
                    Text("  ")
                    JsonItemView(child)
                }
            }
        }
        // end child view [optional]
        item.getEndValue()?.let {
            Text(text = it, color = color)
        }
    }
}


