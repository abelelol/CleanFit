//package com.example.cleanfit.ui.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSerializable
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.runtime.toMutableStateList
//import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
//import androidx.navigation3.runtime.NavBackStack
//import androidx.navigation3.runtime.NavEntry
//import androidx.navigation3.runtime.NavKey
//import androidx.navigation3.runtime.rememberDecoratedNavEntries
//import androidx.navigation3.runtime.rememberNavBackStack
//import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
//import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.PolymorphicSerializer
//import kotlin.collections.emptyList
//
//class NavigationState (
//    val startRoute: NavKey,
//    topLevelRoute: MutableState<NavKey>,
//    val backStack: Map<NavKey, NavBackStack<NavKey>>
//){
//    var topLevelRoute by topLevelRoute
//
//    // top level destinations we can visit
//    val stacksInUse: List<NavKey>
//        get() = if(topLevelRoute == startRoute){
//            listOf(startRoute)
//        } else {
//            listOf(startRoute, topLevelRoute)
//        }
//}
//
//@Composable
//fun rememberNavigationState(
//    startRoute: NavKey,
//    topLevelRoutes: Set<NavKey>
//): NavigationState {
//    val topLevelRoute = rememberSerializable(
//        startRoute,
//        topLevelRoutes,
//        serializer = MutableStateSerializer(PolymorphicSerializer(Route::class))    ) {
//        mutableStateOf(startRoute)
//    }
//    val backStack = topLevelRoutes.associateWith { key ->
//        rememberNavBackStack(key)
//    }
//
//    return remember(startRoute, topLevelRoutes){
//        NavigationState(startRoute, topLevelRoute, backStack)
//    }
//
//}
//
//@Composable
//fun NavigationState.toEntries(
//    entryProvider: (NavKey) ->  NavEntry<NavKey>
//): SnapshotStateList<NavEntry<NavKey>> {
//    val decoratedEntries = backStack.mapValues { (_,stack) ->
//        val decorators = listOf(
//            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
//            rememberViewModelStoreNavEntryDecorator()
//        )
//        rememberDecoratedNavEntries(
//            backStack = stack,
//            entryDecorators = decorators,
//            entryProvider = entryProvider
//        )
//
//    }
//
//    return stacksInUse
//        .flatMap { decoratedEntries[it] ?: emptyList() }
//        .toMutableStateList()
//}
//
//
//

package com.example.cleanfit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.serializer
import kotlin.collections.emptyList
import kotlin.reflect.KClass

// 1. GENERIC CLASS <T>: This is what Philipp uses.
// It adapts to whatever key type you give it (Route, Screen, etc.)
class NavigationState<T : NavKey>(
    val startRoute: T,
    topLevelRoute: MutableState<T>,
    val backStack: Map<T, NavBackStack<NavKey>>
) {
    var topLevelRoute by topLevelRoute

    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

// 2. REIFIED FUNCTION: This is the "Magic" part.
// 'reified T' allows us to see the actual class (Route::class) at runtime
// so the serializer knows exactly what to look for.
@Composable
inline fun <reified T : NavKey> rememberNavigationState(
    startRoute: T,
    topLevelRoutes: Set<T>
): NavigationState<T> {

//    val serializer = PolymorphicSerializer(T::class)
    val serializer = serializer<T>()

    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        // We safely cast the generic serializer to match the expected type
//        serializer = MutableStateSerializer(serializer as KSerializer<T>)
        serializer = MutableStateSerializer(serializer)
    ) {
        mutableStateOf(startRoute)
    }

    val backStack = topLevelRoutes.associateWith { key ->
        rememberNavBackStack(key)
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(startRoute, topLevelRoute, backStack)
    }
}

// 3. GENERIC EXTENSION: Works on NavigationState<T>
@Composable
fun <T : NavKey> NavigationState<T>.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): List<NavEntry<NavKey>> {
    val decoratedEntries = backStack.mapValues { (_, stack) ->
        val decorators = listOf(
            // keeps track of scroll position and other stuff when navigating between screens
            // reminds me of onsaveinstancestate in the fragment lifecycle
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            // basically the same thing but doing it with the viewmodel
            rememberViewModelStoreNavEntryDecorator()
        )
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
