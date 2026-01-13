package com.example.cleanfit.ui.closet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.model.ClosetItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClosetViewModel @Inject constructor(
    private val clothingDao: ClothingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClosetUiState(isLoading = true))
    val uiState: StateFlow<ClosetUiState> = _uiState.asStateFlow()

    private var allItemsCache: List<ClosetItemUi> = emptyList()


    init {
        viewModelScope.launch {
            clothingDao.getAllClothingItems().collect { entities ->
                // Converting Database Entities to UI Items
                val uiItems = entities.map {
                    ClosetItemUi(it.id, it.label, it.imageUri)
                }

                // Update our cache
                allItemsCache = uiItems

                //Show everything by default (filter with empty string)
                filterText("")
            }
        }
    }


    fun filterText(input: String) {
        _uiState.update { currentState ->
            val filtered = if (input.isEmpty()) {
                allItemsCache // Return full list
            } else {
                allItemsCache.filter { it.label.contains(input, ignoreCase = true) }
            }

            currentState.copy(
                items = filtered,
                isLoading = false,
                searchQuery = input
            )
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            clothingDao.deleteById(itemId)
        }
    }

}

data class ClosetUiState(
    val isLoading: Boolean = false,
    val items: List<ClosetItemUi> = emptyList(),
    val searchQuery: String = ""
)