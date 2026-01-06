package com.example.cleanfit.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanfit.data.ClothingImageAnalyzer
import com.example.cleanfit.data.local.dao.ClothingDao
import com.example.cleanfit.data.local.entity.ClothingItem
import com.example.cleanfit.data.model.CameraUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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
    private val analyzer: ClothingImageAnalyzer,
    private val clothingDao: ClothingDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    fun onPhotoCaptured(bitmap: Bitmap, uri: Uri) {
        // 1. Show loading state
//        _uiState.value = _uiState.value.copy(isLoading = true, capturedUri = uri)
        _uiState.update { it.copy(isLoading = true) }


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
                // now using update instead of just calling value to avoid recomposition issues (flash click for example during analyze)
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        showDialog = true,
                        capturedUri = uri,
                        analysisResult = AnalysisResult(
                            category = result.detectedLabel,
                            primaryColor = result.primaryColor,
                            tertiaryColors = result.tertiaryColors
                        )
                    )
                }

            } catch (e: Exception) {
                Log.e("CLEANFIT_AI", "Analysis Failed", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun saveClothingItem(
        context: Context,
        label: String,        // User-edited name (e.g. "Hoodie")
        rawLabel: String,     // AI-detected name (e.g. "Jersey")
        primaryColor: String,
        tertiaryColors: List<String>
    ) {
        val tempUri = _uiState.value.capturedUri ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Create a permanent file in Internal Storage
                // We use filesDir so the OS doesn't delete it when space is low
                val filename = "closet_item_${System.currentTimeMillis()}.jpg"
                val permanentFile = File(context.filesDir, filename)

                // Copy the Cache file (tempUri) to the Permanent file
                context.contentResolver.openInputStream(tempUri)?.use { input ->
                    permanentFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                // Create the Database Entity
                val item = ClothingItem(
                    imageUri = Uri.fromFile(permanentFile).toString(), // Save the PERMANENT path
                    label = label,
                    rawLabel = rawLabel,
                    primaryColor = primaryColor,
                    tertiaryColors = tertiaryColors,
                )

                // Insert into Room
                clothingDao.insertClothingItem(item)

                // E. Close dialog (Navigation happens in UI callback)
                onDialogDismiss()

            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Handle error maybe using a toast.
            }
        }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(showDialog = false) }
    }

    fun onSaveConfirmed(finalResult: AnalysisResult) {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }
}

