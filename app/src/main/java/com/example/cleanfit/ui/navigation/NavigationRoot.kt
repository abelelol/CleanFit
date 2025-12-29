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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.cleanfit.ui.dashboard.HomeScreen
import com.example.cleanfit.ui.login.LoginScreen

data object Home
data class Product(val id: String)


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
){
    val backStack = remember { mutableStateListOf<Any>(Route.LoginRoute) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                // Novel way to hide my navbar if i'm on login screen
                visible = backStack.lastOrNull() != Route.LoginRoute
            ) {
                CleanFitNavBar(
                    selectedKey = (backStack.lastOrNull()) as NavKey,
                    onSelectKey = { backStack.add(it) }
                )
            }

        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Route.LoginRoute> {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear() // You clear out the login screen from the stack/list
                            backStack.add(Route.HomeRoute(it))
                        }
                    )
                }
                entry<Route.HomeRoute> {
                    HomeScreen(name = it.name)
                }
                entry<Route.CameraRoute>{
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Text("Camera")
                    }
                }
                entry<Route.ClosetRoute>{
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Text("Closet")
                    }
                }
                // look into else case for wrong entry
                entry<Route> {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Text("Unknown Route")
                    }
                }
            }

        )


    }

//    NavDisplay(
//        backStack = backStack,
//        onBack = { backStack.removeLastOrNull() },
//        entryProvider = entryProvider {
//            entry<Route.LoginRoute> {
//                LoginScreen(
//                    onLoginSuccess = {
//                        backStack.clear() // You clear out the login screen from the stack/list
//                        backStack.add(Route.HomeRoute(it))
//                    }
//                )
//            }
//            entry<Route.HomeRoute> {
//                HomeScreen(name = it.name)
//            }
//            entry<Route.CameraRoute>{
//                TODO()
//            }
//            entry<Route.ClosetRoute>{
//                TODO()
//            }
//            // look into else case for wrong entry
//            entry<Route> {
//                Text("Unknown route")
//            }
//        }
//
//    )


}