package com.example.cleanfit.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.cleanfit.data.model.OutfitRec
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = HomeUiState(
            userName = "Test User",
            // hard mock these values for now before adding room/supabase implementation to pull data
            // would prob have to look into paging if dataset for a specific user gets to big
            recommendations = listOf(
                OutfitRec(
                    title = "Casual Look",
                    subtitle = "Perfect for a day out",
                    imageUrl = "https://images.unsplash.com/photo-1555760588-58fbd2daf943?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                ),
                OutfitRec(
                    title = "Work Ready",
                    subtitle = "Look sharp at the office",
                    imageUrl = "https://images.unsplash.com/photo-1487222477894-8943e31ef7b2?w=500"
                ),
                OutfitRec(
                    title = "Evening Vibe",
                    subtitle = "For your special night",
                    imageUrl = "https://images.unsplash.com/photo-1512438248247-f0f2a5a8b7f0?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                )
            )
        )
    }
}

data class HomeUiState(
    val userName: String = "",
    val recommendations: List<OutfitRec> = emptyList()
)