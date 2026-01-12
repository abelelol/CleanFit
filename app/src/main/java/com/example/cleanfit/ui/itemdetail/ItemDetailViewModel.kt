package com.example.cleanfit.ui.itemdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.model.ClosetItemUi
import com.example.cleanfit.data.model.ProductRecommendation
import com.example.cleanfit.data.remote.SerperApi
import com.example.cleanfit.data.remote.dto.SerperQuery
import com.example.cleanfit.util.ColorUtils
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
    private val serperApi: SerperApi,
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

            if (item != null) {
                val uiItem = ClosetItemUi(item.id, item.label, item.imageUri)

                // wanted to load base image from db first and not wait on the query
                _uiState.update {
                    it.copy(selectedItem = uiItem)
                }



                val colorName = ColorUtils.getColorNameFromHex(item.primaryColor)
                val search = "$colorName ${ item.label}".trim()
                // limit response to only top 20 for now. will look into pagination for this
                val query = SerperQuery(q = search, country = "us", language = "en", num = 20)

                val recommendations = try {
                    val response = serperApi.searchShopping(query)
                    response.products?.take(4)?.map { dto ->
                        ProductRecommendation(
                            title = dto.title,
                            price = dto.price ?: 0.0,
                            currency = dto.price ?: "USD",
                            productUrl = dto.link,
                            imageUrl = dto.imageUrl ?: "",
                            source = dto.source ?: "Web",
                            rating = 0.0,
                            reviews = 0
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }

                // Update the rest of the viewmodel when they arrive
                _uiState.update {
                    it.copy(
                        recommendations = recommendations ?: emptyList(),
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