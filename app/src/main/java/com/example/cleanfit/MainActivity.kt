package com.example.cleanfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cleanfit.ui.dashboard.HomeScreen
import com.example.cleanfit.ui.login.LoginScreen
import com.example.cleanfit.ui.navigation.NavigationRoot
import com.example.cleanfit.ui.theme.CleanFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // ⚠️ Don't forget this!
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanFitTheme {
                // storing login boolean temporarily for now
                var loggedIn by remember { mutableStateOf(false) }

                if(loggedIn){
                    NavigationRoot()
                } else {
                    LoginScreen(
                        onLoginSuccess = {
                            email -> loggedIn = true
                        }
                    )
                }
            }
        }
    }
}