package com.example.cleanfit.data.model
import java.util.UUID

data class OutfitRec(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

data class ClosetItemUi(
    val id: Long,
    val label: String,      // e.g. "Green Hoodie"
    val imageUrl: String    // e.g. "file:///data/user/0/..."
)
