package com.lsq.adaption.mockscreen

enum class AdaptionUseType {
    MOCK_JSON,

}

enum class ScreenRatios(val index: Int, val typeName: String, val ratio: Float) {
    RATIO_16_9(0, "16:9", 16f / 9),
    RATIO_18_9(1, "18:9", 18f / 9),
    RATIO_19_9(2, "19:9", 19f / 9),
    RATIO_20_9(3, "20:9", 20.025f / 9),
    RATIO_21_9(4, "21:9", 21f / 9),
    RATIO_22_9(5, "22:9", 22f / 9);

    companion object {
        // get all ratios for mock screen
        fun getAllRatios() = ScreenRatios.entries.toTypedArray()
    }
}