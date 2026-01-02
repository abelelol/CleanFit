package com.example.cleanfit.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.cleanfit.ui.camera.CameraScreen
import com.example.cleanfit.ui.dashboard.HomeScreen
import com.example.cleanfit.ui.login.LoginScreen

data object Home
data class Product(val id: String)


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
){
//    val backStack = remember { mutableStateListOf<Any>(Route.LoginRoute) }
    val navigationState = rememberNavigationState(
        startRoute = Route.HomeRoute,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember(navigationState){
        Navigator(navigationState)
    }


    Scaffold(
        modifier = modifier,
        bottomBar = {
//            AnimatedVisibility(
//                // Novel way to hide my navbar if i'm on login screen
//                visible = backStack.lastOrNull() != Route.LoginRoute
//            ) {
                CleanFitNavBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectKey = {
                        navigator.navigate(it as Route)
                    }
                )
//            }

        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBack = navigator::goBack,
            entries = navigationState.toEntries (
                entryProvider {
                    entry<Route.HomeRoute> {
                        // doesnt work atm
                        HomeScreen(
                            onScanClick = { navigator.navigate(Route.CameraRoute) },
                            onClosetClick = { navigator.navigate(Route.ClosetRoute) }
                        )
                    }
                    entry<Route.CameraRoute>{
                        CameraScreen(
                            onImageCaptured = {
                               // navigator.navigate(Route.HomeRoute)
                            },
                            onCloseClick = {
                                navigator.navigate(Route.HomeRoute)
                            }
                        )
                    }
                    entry<Route.ClosetRoute>{
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center) {
                            Text("Closet")
                        }
                    }
                    // look into else case for wrong entry
//                    entry<Route> {
//                        Box(modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center) {
//                            Text("Unknown Route")
//                        }
//                    }
                }
            )
        )

    }



}