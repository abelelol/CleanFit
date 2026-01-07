package com.example.cleanfit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cleanfit.data.local.entity.ClothingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingDao {

    @Query("SELECT * FROM clothing_items ORDER BY dateAdded DESC")
    fun getAllClothingItems(): Flow<List<ClothingItem>> // my understanding flows are basically emitters for data

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClothingItem(item: ClothingItem)

    @Delete
    suspend fun deleteClothingItem(item: ClothingItem)

    @Query("SELECT * FROM clothing_items WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): ClothingItem?

    // Future proofing: Helper to find specific categories
    @Query("SELECT * FROM clothing_items WHERE label = :category ORDER BY dateAdded DESC")
    fun getItemsByCategory(category: String): Flow<List<ClothingItem>>



}