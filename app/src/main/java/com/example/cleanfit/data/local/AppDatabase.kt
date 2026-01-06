package com.example.cleanfit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.local.entity.ClothingItem

@Database(entities = [ClothingItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
}