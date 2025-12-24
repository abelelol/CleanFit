package com.example.cleanfit.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.cleanfit.ui.dashboard.HomeScreen
import com.example.cleanfit.ui.login.LoginScreen

data object Home
data class Product(val id: String)


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
){
//    val backstack = rememberNavBackStack(Route.LoginRoute) // define first screen to start
    val backStack = remember { mutableStateListOf<Any>(Route.LoginRoute) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Route.LoginRoute -> NavEntry(key) {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear() // You clear out the login screen from the stack/list
                            backStack.add(Route.HomeRoute(it))
                        }
                    )
                }

                is Route.HomeRoute -> NavEntry(key) {
                    HomeScreen(name = key.name)
                }
                is Route.CameraRoute -> NavEntry(key) {
                    TODO()
                }
                is Route.ClosetRoute -> NavEntry(key) {
                    TODO()
                }

                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )


}