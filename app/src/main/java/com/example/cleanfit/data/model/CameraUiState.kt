package com.example.cleanfit.data.model

import android.net.Uri
import com.example.cleanfit.ui.camera.AnalysisResult

data class CameraUiState(
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val capturedUri: Uri? = null,
    val analysisResult: AnalysisResult = AnalysisResult(),

//    // Dialog PopUp checks
//    val showAddItemDialog: Boolean = false,

)
