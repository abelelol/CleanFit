package com.example.cleanfit.util

import android.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.core.graphics.toColorInt

object ColorUtils {

    // A small map of standard colors to check against
    private val colorMap = mapOf(
        "Black" to Color.BLACK,
        "White" to Color.WHITE,
        "Red" to Color.RED,
        "Blue" to Color.BLUE,
        "Green" to Color.GREEN,
        "Yellow" to Color.YELLOW,
        "Cyan" to Color.CYAN,
        "Magenta" to Color.MAGENTA,
        "Gray" to Color.GRAY,
        "Orange" to "#FFA500".toColorInt(),
        "Purple" to "#800080".toColorInt(),
        "Pink" to "#FFC0CB".toColorInt(),
        "Brown" to "#A52A2A".toColorInt(),
        "Navy" to "#000080".toColorInt(),
        "Beige" to "#F5F5DC".toColorInt()
    )

    // method to help me translate hex values into simple colors for search

    fun getColorNameFromHex(hex: String?): String {
        if (hex.isNullOrEmpty()) return ""

        try {
            val targetColor = Color.parseColor(hex)
            val r1 = Color.red(targetColor)
            val g1 = Color.green(targetColor)
            val b1 = Color.blue(targetColor)

            var closestColorName = ""
            var minDistance = Double.MAX_VALUE

            for ((name, colorInt) in colorMap) {
                val r2 = Color.red(colorInt)
                val g2 = Color.green(colorInt)
                val b2 = Color.blue(colorInt)

                // Calculate Euclidean distance between the two colors
                val distance = sqrt(
                    (r2 - r1).toDouble().pow(2.0) +
                            (g2 - g1).toDouble().pow(2.0) +
                            (b2 - b1).toDouble().pow(2.0)
                )

                if (distance < minDistance) {
                    minDistance = distance
                    closestColorName = name
                }
            }
            return closestColorName
        } catch (e: Exception) {
            return ""
        }
    }
}