package com.example.cleanfit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://ppscsdadrnxwimnqhzuh.supabase.co",
            supabaseKey = "YOUR_SUPABASE_ANON_KEY_HERE" // ⚠️ Paste your actual key here
        ) {
            install(Postgrest)
            install(Auth)
            // Ktor CIO engine is automatically picked up because we added the dependency!
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}