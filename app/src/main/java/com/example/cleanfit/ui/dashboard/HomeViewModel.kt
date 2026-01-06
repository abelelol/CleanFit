package com.example.cleanfit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.model.ClosetItemUi
import com.example.cleanfit.data.model.OutfitRec
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clothingDao: ClothingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(userName = "Abele") }
        observeRoomDb()

    }
    private fun observeRoomDb(){
        viewModelScope.launch {
            clothingDao.getAllClothingItems().collectLatest { items ->
                val recentItems = items.map {
                    ClosetItemUi(
                        id = it.id,
                        label = it.label,
                        imageUrl = it.imageUri
                    )
                }

                _uiState.update {
                    it.copy(recentItems = recentItems)
                }

            }


        }

    }
}



data class HomeUiState(
    val userName: String = "",
    val recentItems: List<ClosetItemUi> = emptyList()
)