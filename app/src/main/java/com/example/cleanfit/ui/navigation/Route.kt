package com.example.cleanfit.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    // reminds of me how web routes are done in angular

    //Initial login screen
    @Serializable
    data object LoginRoute : Route, NavKey

    // the main screen after login
    @Serializable
    data class HomeRoute(val name: String) : Route, NavKey

    // screen to open camera view to take a picture
    @Serializable
    data object CameraRoute : Route, NavKey

    // screen to see all stores photos taken from app
    @Serializable
    data object ClosetRoute : Route, NavKey



}


