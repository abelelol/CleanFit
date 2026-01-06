package com.example.cleanfit.di

import android.content.Context
import androidx.room.Room
import com.example.cleanfit.data.local.AppDatabase
import com.example.cleanfit.data.local.dao.ClothingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cleanfit_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideClothingDao(database: AppDatabase): ClothingDao {
        return database.clothingDao()
    }
}