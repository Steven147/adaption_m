package com.ss.android.ugc.aweme

import androidx.compose.ui.graphics.Color
import com.ss.android.ugc.aweme.JsonItemType.Companion.getType
import org.json.JSONArray
import org.json.JSONObject

data class JsonItem(
    var key: String? = null,
    var value: Any? = null,
) {
    val valueType: JsonItemType = value.getType()
    //
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
    //
    fun getChildItemList(): Iterable<JsonItem>? {
        return when (valueType) {
            JsonItemType.OBJECT -> {
                val obj = value as JSONObject
                val list = mutableListOf<JsonItem>()
                for (key in obj.keys()) {
                    list.add(
                        JsonItem(key = key, value = obj.opt(key))
                    )
                }
                list
            }
            JsonItemType.ARRAY -> {
                val array = value as JSONArray
                val list = mutableListOf<JsonItem>()
                for (i in 0 until array.length()) {
                    list.add(
                        JsonItem(key = i.toString(), value = array.opt(i))
                    )
                }
                list
            }
            else -> null
        }
    }
    //
    fun getEndValue(): String? {
        return when (valueType) {
            JsonItemType.OBJECT -> "}"
            JsonItemType.ARRAY -> "]"
            else -> null
        }
    }
}

data class JsonItemState(
    var isExpanded: Boolean = false,
)

enum class JsonItemType(
    private val color: Color,
) {
    OBJECT(Color.Red), // { k:v, k2,v2 }
    ARRAY(Color.LightGray), // [ v, v2 ]
    STRING(Color.Green),
    NUMBER(Color.Blue),
    BOOLEAN(Color.Cyan), // true false
    NULL(Color.Black);

    companion object {

        fun Any?.getType(): JsonItemType {
            return when (this) {
                is JSONObject -> OBJECT
                is JSONArray -> ARRAY
                is String -> STRING
                is Number -> NUMBER
                is Boolean -> BOOLEAN
                else -> NULL
            }
        }
    }

    // 根据类型获取颜色
    fun getColor(): Color {
        return this.color
    }
}



