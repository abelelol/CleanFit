package com.example.cleanfit.data.remote

import com.example.cleanfit.data.remote.dto.SerperQuery
import com.example.cleanfit.data.remote.dto.SerplyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface SerperApi {
//    @GET("v1/product/search/q={query}")
//    suspend fun searchProducts(
//        @Path("query") query: String
//    ): SerplyResponse


    @POST("/shopping")
    suspend fun searchShopping(
        @Body body: SerperQuery
    ): SerplyResponse
}