//package com.example.cleanfit.ui.navigation
//
//import androidx.navigation3.runtime.NavKey
//
//class Navigator(val state: NavigationState){
//
//    fun navigate(route: NavKey){
//        if(route in state.backStack.keys){
//            state.topLevelRoute = route
//        } else {
//            state.backStack[state.topLevelRoute]?.add(route)
//        }
//    }
//
//    fun goBack(){
//        val currentStack = state.backStack[state.topLevelRoute]
//            ?: error("Backstack for ${state.topLevelRoute} doesn't exist")
//
//        val currentRoute = currentStack.last()
//
//        if( currentRoute == state.topLevelRoute){
//            state.topLevelRoute = state.startRoute
//        } else {
//           currentStack.removeLastOrNull()
//        }
//
//
//    }
//}

package com.example.cleanfit.ui.navigation

import androidx.navigation3.runtime.NavKey

// Add <T : NavKey> here too
class Navigator<T : NavKey>(private val state: NavigationState<T>) {

    fun navigate(route: T) {
        if (route in state.backStack.keys) {
            state.topLevelRoute = route
        } else {
            state.backStack[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStack[state.topLevelRoute]
            ?: error("Backstack for ${state.topLevelRoute} doesn't exist")

        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}