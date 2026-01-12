package com.example.cleanfit.data.model

data class ProductRecommendation(
    val title: String?,
    val price: Any,
    val currency: String,
    val imageUrl: String,
    val productUrl: String?,
    val source: String,
    val rating: Double = 0.0, // Default to 0 if missing
    val reviews: Int = 0      // Default to 0 if missing

)
