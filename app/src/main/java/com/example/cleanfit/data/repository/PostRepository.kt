package com.example.cleanfit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val authRepository: AuthRepository
) {
    suspend fun createPost(content: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val accessToken = authRepository.getCurrentUserToken()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val url = "https://ppscsdadrnxwimnqhzuh.supabase.co/functions/v1/insert-post"
                val anonKey = "YOUR_SUPABASE_ANON_KEY_HERE" // ⚠️ Paste Key Here

                val jsonBody = """{"content": "$content"}"""
                val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .header("Authorization", "Bearer $accessToken")
                    .header("apikey", anonKey)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (response.isSuccessful) {
                    Result.success(response.body?.string() ?: "Success")
                } else {
                    Result.failure(Exception("Error: ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}