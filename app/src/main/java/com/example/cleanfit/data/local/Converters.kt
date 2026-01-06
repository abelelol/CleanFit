package com.example.cleanfit.data.local

import androidx.room.TypeConverter

//Helps to store tertiary colors in the database since we cant use lists as a type

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}