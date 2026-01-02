package com.example.cleanfit.data.model

data class ClothingAnalysis(
    val detectedLabel: String,
    val confidence: Float,
    val primaryColor: String,
    val tertiaryColors: MutableList<String>

)
