package com.example.cleanfit.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerplyResponse(
    @param:Json(name = "shopping") val products: List<SerperItemDto>? = null,
)

@JsonClass(generateAdapter = true)
data class SerperItemDto(
    @param:Json(name = "title") val title: String?,
    @param:Json(name = "price") val price: String?,
    @param:Json(name = "source") val source: String?,
    @param:Json(name = "imageUrl") val imageUrl: String?,
    @param:Json(name = "link") val link: String?,

    @param:Json(name = "rating") val rating: Double? = 0.0,
    @param:Json(name = "ratingCount") val reviewCount: Int? = 0
)

