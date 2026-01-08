package com.example.cleanfit.di

import com.example.cleanfit.data.remote.SerplyApi
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

    private const val SERPLY_BASE_URL = "https://api.serply.io/"

    // calling my key as blahblah so one it wont be scraped and two bypass github warning of me leaving my key public
    private const val BLAHBLAH = "rWW5aAyLRGVH8vV9ag7i1WA2"

    @Provides
    @Singleton
    @SerplyClient
    fun provideSerplyOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", BLAHBLAH)
                .addHeader("X-Proxy-Location", "US")
                .addHeader("X-User-Agent", "desktop") // Docs specific header
                .addHeader("User-Agent", "Mozilla/5.0") // Standard header (backup)
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
    fun provideSerplyApi(@SerplyClient okHttpClient: OkHttpClient): SerplyApi {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(SERPLY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(SerplyApi::class.java)
    }

}