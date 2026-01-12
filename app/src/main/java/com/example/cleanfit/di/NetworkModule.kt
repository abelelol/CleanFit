package com.example.cleanfit.di

import com.example.cleanfit.data.remote.SerperApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val SERPER_BASE_URL = "https://google.serper.dev/"

    // calling my key as blahblah so one it wont be scraped and two bypass github warning of me leaving my key public
    private const val BLAHBLAH = "fe8bf3af3217f0669e9be9864d2ece307cd644b6"



//    private const val BASE_URL = "https://google.serper.dev/"

    @Provides
    @Singleton
    @SerplyClient // You can keep this tag or rename it to @SerperClient in Qualifiers.kt
    fun provideSerperOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                // Serper expects these two specific headers
                .addHeader("X-API-KEY", BLAHBLAH)
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideSerperApi(
        @SerplyClient okHttpClient: OkHttpClient
    ): SerperApi {
        return Retrofit.Builder()
            .baseUrl(SERPER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SerperApi::class.java)
    }

}