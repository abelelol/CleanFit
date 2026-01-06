package com.example.cleanfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothing_items")
data class ClothingItem (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val imageUri: String, // path to saved photo
    val label: String, // added this attribute in case ml gave bad response and we have to use user defined label
    val rawLabel: String = "", // ml defined label of image

    // good practice of me would probably also have separate attributes for user defined colors but
    // im lazy lol and this is a side project
    val primaryColor: String, // dominant color of clothing item
    val tertiaryColors: List<String>, // palette of colors of clothing item

    val dateAdded: Long = System.currentTimeMillis()

    // val userId: String? = null // current only doing local storage so no need for this attribute atm


)




