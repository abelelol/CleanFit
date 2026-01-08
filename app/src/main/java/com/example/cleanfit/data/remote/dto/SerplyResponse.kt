package com.example.cleanfit.data.remote.dto

import com.squareup.moshi.Json

data class SerplyResponse(
    @param:Json(name = "products") val products: List<SerplyProductDto>? = null,

    // CASE 2 Empty/Fallback
    @param:Json(name = "results") val results: List<SerplyProductDto>? = null)

// The individual item from Serply
data class SerplyProductDto(
    @param:Json(name = "title") val title: String,
    @param:Json(name = "price") val price: Double?,
    @param:Json(name = "currency") val currency: String? = "USD",
    @param:Json(name = "link") val link: String,
    @param:Json(name = "image") val imageUrl: String?,
    @param:Json(name = "source") val source: String? = "Unknown"
)