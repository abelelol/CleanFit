package com.example.cleanfit.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.repository.AuthRepository
import com.example.cleanfit.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    var uiMessage by mutableStateOf<String?>(null)
    var email by mutableStateOf("")

    fun onGoogleSignInResult(idToken: String, rawNonce: String) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken, rawNonce)
                .onSuccess { uiMessage = "Signed in successfully!" }
                .onFailure { uiMessage = "Sign in failed: ${it.message}" }
        }
    }

    fun onInsertPostClicked() {
        viewModelScope.launch {
            postRepository.createPost("Hello from Clean Architecture!")
                .onSuccess { uiMessage = "Edge Function: $it" }
                .onFailure { uiMessage = "Failed: ${it.message}" }
        }
    }
}