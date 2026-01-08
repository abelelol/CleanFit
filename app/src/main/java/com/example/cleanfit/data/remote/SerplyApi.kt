package com.example.cleanfit.data.remote

import com.example.cleanfit.data.remote.dto.SerplyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SerplyApi {
    @GET("v1/product/search/q={query}")
    suspend fun searchProducts(
        @Path("query") query: String
    ): SerplyResponse
}