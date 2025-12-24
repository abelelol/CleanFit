package com.example.cleanfit.ui.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject // 2. Add this import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _userName = MutableStateFlow<String>("Test User")
    val userName = _userName.asStateFlow()

    fun setUserName(name: String) {
        _userName.value = name
    }
}

