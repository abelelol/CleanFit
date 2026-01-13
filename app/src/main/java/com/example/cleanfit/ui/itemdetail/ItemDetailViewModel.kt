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

    // Internal cache to hold ALL fetched items
    private val allRecommendationsCache = mutableListOf<ProductRecommendation>()

    // Pagination tracking
    private var currentSearchQuery: String = ""
    private var currentPage = 1


    // Call this when the screen opens
//    fun loadItem(itemId: Long) {
//        viewModelScope.launch {
//            val item = clothingDao.getItemById(itemId)
//
//            if (item != null) {
//                val uiItem = ClosetItemUi(item.id, item.label, item.imageUri)
//
//                // wanted to load base image from db first and not wait on the query
//                _uiState.update {
//                    it.copy(
//                        selectedItem = uiItem,
//                        isLoading = false,
//                        isShoppingLoading = true
//                    )
//                }
//
//
//
//                val colorName = ColorUtils.getColorNameFromHex(item.primaryColor)
//                val search = "$colorName ${ item.label}".trim()
//                // limit response to only top 20 for now. will look into pagination for this
//                val query = SerperQuery(q = search, country = "us", language = "en", num = 20)
//
//                val recommendations = try {
//                    val response = serperApi.searchShopping(query)
//                    response.products?.take(4)?.map { dto ->
//                        ProductRecommendation(
//                            title = dto.title,
//                            price = dto.price ?: 0.0,
//                            currency = dto.price ?: "USD",// currency is actually not needed, will need to clean this up later
//                            productUrl = dto.link,
//                            imageUrl = dto.imageUrl ?: "",
//                            source = dto.source ?: "Web",
//                            rating = 0.0,
//                            reviews = 0
//                        )
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    emptyList()
//                }
//
//                // Update the rest of the viewmodel when they arrive
//                _uiState.update {
//                    it.copy(
//                        recommendations = recommendations ?: emptyList(),
//                        isLoading = false,
//                        isShoppingLoading = false
//                    )
//                }
//            }
//        }
//    }

    fun loadItem(itemId: Long) {
        viewModelScope.launch {
            val item = clothingDao.getItemById(itemId)

            if (item != null) {
                val uiItem = ClosetItemUi(item.id, item.label, item.imageUri)

                // Save query for pagination later
                val colorName = ColorUtils.getColorNameFromHex(item.primaryColor)
                currentSearchQuery = "$colorName ${item.label}".trim()

                _uiState.update {
                    it.copy(
                        selectedItem = uiItem,
                        isLoading = false,
                        isShoppingLoading = true
                    )
                }

                // Reset page to 1 for new item
                currentPage = 1
                fetchShoppingResults(isFirstPage = true)
            }
        }
    }

    private suspend fun fetchShoppingResults(isFirstPage: Boolean) {
        // Only pass 'page' if it's greater than 1
        val query = SerperQuery(
            q = currentSearchQuery,
            country = "us",
            language = "en",
            num = 10,
            page = currentPage.toString()
        )

        try {
            val response = serperApi.searchShopping(query)

            val newItems = response.products?.map { dto ->
                ProductRecommendation(
                    title = dto.title,
                    price = dto.price.toString(),
                    currency = "$",
                    productUrl = dto.link,
                    imageUrl = dto.imageUrl ?: "",
                    source = dto.source ?: "Web",
                    rating = dto.rating ?: 0.0,
                    reviews = dto.reviewCount ?: 0
                )
            } ?: emptyList()

            // If it's a fresh load, clear old cache
            if (isFirstPage) {
                allRecommendationsCache.clear()
            }
            allRecommendationsCache.addAll(newItems)

            updateUiList()

        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.update { it.copy(isShoppingLoading = false, isLoadingMore = false) }
        }
    }

    // Helper to update what the UI sees based on "View All" mode
    private fun updateUiList() {
        _uiState.update { state ->
            state.copy(
                recommendations = if (state.isViewAllMode) {
                    allRecommendationsCache.toList() // Show everything
                } else {
                    allRecommendationsCache.take(4) // Show preview only
                },
                isShoppingLoading = false,
                isLoadingMore = false
            )
        }
    }

    // Called when "View All" is clicked
    fun toggleViewAll() {
        _uiState.update {
            it.copy(isViewAllMode = !it.isViewAllMode)
        }
        updateUiList() // Refresh list based on new mode
    }

    // Called when scrolling to the bottom
    fun loadNextPage() {
        // Prevent duplicate calls or calls when closed
        if (uiState.value.isLoadingMore || uiState.value.isShoppingLoading) return
        if (!uiState.value.isViewAllMode) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            currentPage++ // Increment Page
            fetchShoppingResults(isFirstPage = false)
        }
    }
}

data class ItemDetailUiState(
    val isLoading: Boolean = true,
    val isShoppingLoading: Boolean = false,
    val selectedItem: ClosetItemUi? = null,
    val isLoadingMore: Boolean = false,     // Pagination Load
    val isViewAllMode: Boolean = false,     // Toggle view all/view more state
    val recommendations: List<ProductRecommendation> = emptyList()
)