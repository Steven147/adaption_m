package com.ss.android.ugc.aweme

import androidx.compose.ui.graphics.Color
import com.ss.android.ugc.aweme.JsonItemType.Companion.getType
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

data class JsonItem(
    var key: String? = null,
    var value: JsonElement? = null
) {
    val valueType: JsonItemType = value.getType()

    fun getCurrentValue(): String {
        return when (valueType) {
            JsonItemType.OBJECT -> "{"
            JsonItemType.ARRAY -> "["
            JsonItemType.STRING,
            JsonItemType.NUMBER,
            JsonItemType.BOOLEAN -> value.toString()
            JsonItemType.NULL -> "null"
        }
    }

    fun getChildItemList(): Iterable<JsonItem>? {
        return when (valueType) {
            JsonItemType.OBJECT -> {
                val obj = value as JsonObject
                val list = mutableListOf<JsonItem>()
                for ((key, element) in obj) {
                    list.add(
                        JsonItem(key = key, value = element)
                    )
                }
                list
            }
            JsonItemType.ARRAY -> {
                val array = value as JsonArray
                val list = mutableListOf<JsonItem>()
                for ((index, element) in array.withIndex()) {
                    list.add(
                        JsonItem(key = index.toString(), value = element)
                    )
                }
                list
            }
            else -> null
        }
    }

    fun getEndValue(): String? {
        return when (valueType) {
            JsonItemType.OBJECT -> "}"
            JsonItemType.ARRAY -> "]"
            else -> null
        }
    }
}

data class JsonItemState(
    var isExpanded: Boolean = false
)

enum class JsonItemType(
    private val color: Color
) {
    OBJECT(Color.Red), // { k:v, k2,v2 }
    ARRAY(Color.LightGray), // [ v, v2 ]
    STRING(Color.Green),
    NUMBER(Color.Blue),
    BOOLEAN(Color.Cyan), // true false
    NULL(Color.Black);

    companion object {
        fun JsonElement?.getType(): JsonItemType {
            return when (this) {
                is JsonObject -> OBJECT
                is JsonArray -> ARRAY
                is JsonPrimitive -> {
                    when {
                        isString -> STRING
                        content.toDoubleOrNull() != null -> NUMBER
                        content.toBooleanStrictOrNull() != null -> BOOLEAN
                        else -> NULL
                    }
                }
                is JsonNull -> NULL
                null -> NULL
            }
        }
    }

    // 根据类型获取颜色
    fun getColor(): Color {
        return this.color
    }
}