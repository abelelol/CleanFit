package com.example.cleanfit.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DoorSliding
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (
    val icon: ImageVector,
    val title: String
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    (Route.HomeRoute) as Object to BottomNavItem(
        icon = Icons.Filled.Home,
        title = "Home"
    ),
    Route.CameraRoute to BottomNavItem(
        icon = Icons.Filled.CameraAlt,
        title = "camera"
    ),
    Route.ClosetRoute to BottomNavItem(
        icon = Icons.Filled.DoorSliding,
        title = "Closet"
    )


)

