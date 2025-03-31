package com.ss.android.ugc.aweme

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.json.JSONArray
import org.json.JSONObject

@Preview(showBackground = true)
@Composable
fun PreviewJsonItemView() {
    val obj = JSONObject().apply {
        put("str", "string")
        put("num", 123)
        put("bool", true)
        put("null", null)
        put("array", JSONArray().apply {
            put("string")
            put(123)
            put(true)
            put(null)
            put(JSONObject().apply {
                put("str", "string")
                put("num", 123)
                put("bool", true)
                put("null", null)
            })
        })
        put("obj", JSONObject().apply {
            put("str", "string")
            put("num", 123)
            put("bool", true)
            put("null", null)
        })
    }
    Column {
        Text(text = obj.toString())
        JsonItemView(
            item = JsonItem(
                key = "root",
                value = JSONObject(obj.toString())
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
                    Log.e("JsonItemView","key is null")
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


