package com.example.cleanfit.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun signInWithGoogle(idToken: String, rawNonce: String): Result<Unit> {
        return try {
            supabase.auth.signInWith(IDToken) {
                this.idToken = idToken
                this.provider = Google
                this.nonce = rawNonce
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserToken(): String? {
        return supabase.auth.currentSessionOrNull()?.accessToken
    }
}