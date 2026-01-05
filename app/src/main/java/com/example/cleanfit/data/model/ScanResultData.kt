package com.example.cleanfit.data.model

import androidx.compose.ui.graphics.Color

data class ScanResultData(
    val itemType: String,
//    val dominantColorName: String,
    val dominantColorHex: String,
    val tertiaryColorHexes: List<String>
)
