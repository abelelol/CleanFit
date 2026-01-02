package com.example.cleanfit.data

import android.graphics.Bitmap
import android.util.Log
import com.example.cleanfit.data.model.ClothingAnalysis
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Singleton
class ClothingImageAnalyzer @Inject constructor() {


    // Base ML Kit model is atrocious omg, look into custom models
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    // Need to look into Coroutines/Dispatcher, Understanding so far is that your
    // breaking down a thread into small mini threads
    suspend fun analyze(bitmap: Bitmap): ClothingAnalysis = withContext(Dispatchers.Default) {

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        val labels = try {
            labeler.process(inputImage).await()
        } catch (e: Exception) {
            emptyList()
        }

        Log.d("CLEANFIT_AI", "--- Full ML Kit Results ---")
        labels.forEach { label ->
            // e.g. "Found: Jersey (0.94)"
            Log.d("CLEANFIT_AI", "Found: ${label.text} (${label.confidence})")
        }
        Log.d("CLEANFIT_AI", "---------------------------")

        // 3. Get best result
        val topLabel = labels
            .maxByOrNull { it.confidence }
            ?.text ?: "Unknown"


        // I'll identify the colors later.
        ClothingAnalysis(
            detectedLabel = topLabel,
            confidence = labels.firstOrNull()?.confidence ?: 0f,
            primaryColor = "Unknown",
            tertiaryColors = mutableListOf()
        )
    }
}