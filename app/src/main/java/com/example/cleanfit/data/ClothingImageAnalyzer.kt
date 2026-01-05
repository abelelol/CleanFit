package com.example.cleanfit.data

import android.graphics.Bitmap
import android.util.Log
import androidx.palette.graphics.Palette
import com.example.cleanfit.data.model.ClothingAnalysis
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import javax.inject.Inject // OR jakarta.inject depending on your setup
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class ClothingImageAnalyzer @Inject constructor() {

    private val localModel = LocalModel.Builder()
        .setAssetFilePath("test.tflite") // Using Mobile_Net_V3 model, base mlkit is atrocious for object identification
        .build()

    // keeping confidence low atm, might change later.
    private val customOptions = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.1f)
        .setMaxResultCount(15)
        .build()

    // Lazy load to prevent startup crash
    // might need to look into my code logcat is saying I'm skipping 59 frames
    // so I might have to many things on the main thread
    private val labeler by lazy {
        ImageLabeling.getClient(customOptions)
    }

    suspend fun analyze(bitmap: Bitmap): ClothingAnalysis = withContext(Dispatchers.Default) {

        // Check if the image is actually valid (had some issues with this earlier)
        Log.d("CLEANFIT_AI", "Analyzing Image Size: ${bitmap.width} x ${bitmap.height}")

        val inputImage = InputImage.fromBitmap(bitmap, 0)

        val labels = try {
            labeler.process(inputImage).await()
        } catch (e: Exception) {
            Log.e("CLEANFIT_AI", "Model Failed", e)
            emptyList()
        }

        Log.d("CLEANFIT_AI", "--- Results Found: ${labels.size} ---")
        labels.forEach {
            Log.d("CLEANFIT_AI", "Found: ${it.text} (${it.confidence})")
        }

        // Filter for Clothing might expand later (got back diaper at one point for a white tshirt lol)
        val clothingKeywords = setOf(
            "jersey", "shirt", "blouse", "jean", "sweatshirt", "cardigan",
            "velvet", "wool", "sandal", "shoe", "boot", "sneaker",
            "jacket", "coat", "dress", "vest", "skirt", "short", "sock",
            "gown", "hat", "trousers", "hoodie", "pullover", "t-shirt",
            "clothing", "garment", "textile"
        )

        val bestLabel = labels.firstOrNull { label ->
            clothingKeywords.any { k -> label.text.lowercase().contains(k) }
        } ?: labels.maxByOrNull { it.confidence }


        // now we get colors from the image (Color Palette Api is the best and only option sadly)
        val palette = Palette.from(bitmap).generate()

        // Getting the primary color
        // If null (rare), default to Black .
        val dominantInt = palette.getDominantColor(android.graphics.Color.BLACK)
        val dominantHex = toHex(dominantInt)

        // Get Accent Colors (Vibrant, Muted, etc.) for the "Tertiary" list
        val tertiaryList = mutableListOf<String>()

        // https://developer.android.com/develop/ui/views/graphics/palette-colors
        // basically this api identifies the the predominant color in each color category and I'll be using said
        // colors as my tertiary colors. Pretty cool honestly.

        palette.vibrantSwatch?.let { tertiaryList.add(toHex(it.rgb)) }
        palette.darkVibrantSwatch?.let { tertiaryList.add(toHex(it.rgb)) }
        palette.lightVibrantSwatch?.let { tertiaryList.add(toHex(it.rgb)) }
        palette.mutedSwatch?.let { tertiaryList.add(toHex(it.rgb)) }

        // log all the colors
        Log.d("CLEANFIT_AI", "Dominant Color: $dominantHex")
        tertiaryList.forEach {
            Log.d("CLEANFIT_AI", "Tertiary Color: $it")
        }



        //
        // If bestLabel is null, default to "Unknown"
        val finalLabel = bestLabel?.text ?: "Unknown Object"
        val finalConfidence = bestLabel?.confidence ?: 0f



        ClothingAnalysis(
            detectedLabel = finalLabel,
            confidence = finalConfidence,
            primaryColor = dominantHex,
            tertiaryColors = mutableListOf()
        )

    }
    // Helper to convert Android Color Int to Hex String (e.g. #FF0000)
    private fun toHex(color: Int): String {
        return String.format("#%06X", (0xFFFFFF and color))
    }
}