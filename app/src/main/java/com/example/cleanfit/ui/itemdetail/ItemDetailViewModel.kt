package com.example.cleanfit.ui.itemdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.model.ClosetItemUi
import com.example.cleanfit.data.model.ProductRecommendation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val clothingDao: ClothingDao,
    savedStateHandle: SavedStateHandle // To get the arguments passed from navigation
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailUiState())
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    init {
        // will hook this up to the real argument later.
    }

    // Call this when the screen opens
    fun loadItem(itemId: Long) {
        viewModelScope.launch {
            val item = clothingDao.getItemById(itemId)

            // Mock Recommendations till get I get serply working
            val recommendations = listOf(
                ProductRecommendation(
                    title = "Women's Red Cocktail Dress",
                    price = 59.99,
                    currency = "USD",
                    productUrl = "https://www.macys.com",
                    imageUrl = "https://images.unsplash.com/photo-1595777457583-95e059d581b8",
                    source = "Macy's",
                    rating = 4.6,
                    reviews = 134
                ),
                ProductRecommendation(
                    title = "Red Summer Maxi Dress",
                    price = 39.99,
                    currency = "USD",
                    productUrl = "https://www.asos.com",
                    imageUrl = "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1",
                    source = "ASOS",
                    rating = 0.0,
                    reviews = 0
                )
            )

            if (item != null) {
                _uiState.update {
                    it.copy(
                        selectedItem = ClosetItemUi(item.id, item.label, item.imageUri),
                        recommendations = recommendations,
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class ItemDetailUiState(
    val isLoading: Boolean = true,
    val selectedItem: ClosetItemUi? = null,
    val recommendations: List<ProductRecommendation> = emptyList()
)