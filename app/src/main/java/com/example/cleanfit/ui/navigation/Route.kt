package com.example.cleanfit.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route : NavKey {

    // reminds of me how web routes are done in angular

    //Initial login screen
    @Serializable
    data object LoginRoute : Route

    // the main screen after login
    @Serializable
    data object HomeRoute : Route

    // screen to open camera view to take a picture
    @Serializable
    data object CameraRoute : Route

    // screen to see all stores photos taken from app
    @Serializable
    data object ClosetRoute : Route

    // screen to view recommended clothing based on picture
    @Serializable
    data class ItemDetailRoute(val itemId: Long) : Route



}


