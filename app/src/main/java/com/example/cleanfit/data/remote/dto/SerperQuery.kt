package com.example.cleanfit.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerperQuery(
    @param:Json(name = "q") val q: String, // the search query
    @param:Json(name = "gl") val country: String = "us", // TODO Should change this later to region based on users profile region
    @param:Json(name = "hl") val language: String = "en",
    @param:Json(name = "num") val num: Int = 10          // Number of results (10-100) higher number reduce my credits so keeping it low for now
)

