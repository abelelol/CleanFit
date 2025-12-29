package com.example.cleanfit.ui.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class NavigationState (
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStack: Map<NavKey, NavBackStack<NavKey>>
){
    var topLevelRoute by topLevelRoute
    val stacksInUse: List<NavKey>
        get() = if(topLevelRoute == startRoute){
            listOf(startRoute)
        } else {
            listOf(startRoute, startRoute)
        }
}
