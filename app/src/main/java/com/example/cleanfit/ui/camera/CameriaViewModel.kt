package com.example.cleanfit.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.ClothingImageAnalyzer
import com.example.cleanfit.data.model.CameraUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Data class to hold the analyzed result
data class AnalysisResult(
    val category: String = "",
    val primaryColor: String = "",
    val tertiaryColors: List<String> = emptyList()
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyzer: ClothingImageAnalyzer
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoCaptured(bitmap: Bitmap, uri: Uri) {
        // 1. Show loading state
        _uiState.value = _uiState.value.copy(isLoading = true, capturedUri = uri)


        viewModelScope.launch {
            try {
                Log.d("CLEANFIT_AI", "Analyzing image...")

                // 2. Run the Analysis
                val result = analyzer.analyze(bitmap)

                // 3. Print to Logcat
                Log.d("CLEANFIT_AI", "---------------------------------")
                Log.d("CLEANFIT_AI", "SUCCESS! Object Identified:")
                Log.d("CLEANFIT_AI", "Label: ${result.detectedLabel}")
                Log.d("CLEANFIT_AI", "Confidence: ${result.confidence}")
                Log.d("CLEANFIT_AI", "---------------------------------")

                // Update state (even if we aren't showing the dialog yet)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    analysisResult = AnalysisResult(
                        category = result.detectedLabel,
                        primaryColor = result.primaryColor,
                        tertiaryColors = result.tertiaryColors
                    ),
                    showDialog = true
                )

            } catch (e: Exception) {
                Log.e("CLEANFIT_AI", "Analysis Failed", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
//
//        viewModelScope.launch {
//            // TODO: REPLACE THIS WITH ACTUAL ML KIT CALLS
//            // mimic analysis delay
//            delay(1500)
//
//            // 2. Mock Analysis Result
//            val mockResult = AnalysisResult(
//                category = "T-Shirt",
//                primaryColor = "Navy Blue",
//                tertiaryColor = "White"
//            )
//
//            // 3. Update state to show Dialog
//            _uiState.value = _uiState.value.copy(
//                isLoading = false,
//                showDialog = true,
//                analysisResult = mockResult
//            )
//        }
    }

    fun onDialogDismiss() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }

    fun onSaveConfirmed(finalResult: AnalysisResult) {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }
}

