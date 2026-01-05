package com.example.cleanfit.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField // Needed for inline editing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.cleanfit.data.model.ScanResultData
import androidx.core.graphics.toColorInt
import android.graphics.Color as AndroidColor

// Needed to track where the color picker result goes
enum class ColorPickerTarget {
    PRIMARY,
    TERTIARY
}

@Composable
fun ScanResultDialog(
    // Controls visibility
    showDialog: Boolean,
    // The data to display
    data: ScanResultData,
    // Callbacks for actions
    onDismissRequest: () -> Unit,
    onEditItemTypeClick: () -> Unit,
    // CHANGED: Added String param to pass back the edited Item Name
    onAddToClosetClick: (String, String, List<String>) -> Unit,
    onRetakePhotoClick: () -> Unit
) {
    if (showDialog) {

        // CHANGED: State for editing item name (Always editable now)
        var currentItemType by remember { mutableStateOf(data.itemType) }

        var dominantColorHex by remember { mutableStateOf(data.dominantColorHex) }

//
//        val paletteList = remember(data) {
//            (listOf(data.dominantColorHex) + data.tertiaryColorHexes).distinct()
//        }
        val currentPalette = remember(data) {
            mutableStateListOf<String>().apply {
                addAll(data.tertiaryColorHexes.distinct())
                if (isEmpty()) add(data.dominantColorHex)
            }
        }

        // CHANGED: Renamed to avoid confusion. Tracks the highlighted circle in the bottom list.
        var activeTertiaryColorHex by remember { mutableStateOf(currentPalette.first()) }


        var showColorPicker by remember { mutableStateOf(false) }
        var pickerTarget by remember { mutableStateOf(ColorPickerTarget.PRIMARY) }

        // Handle the color picker result based on who asked for it
        val onNewColorPicked: (String) -> Unit = { newHex ->
            if (pickerTarget == ColorPickerTarget.PRIMARY) {
                // Edit ROW 1 (Primary/Dominant)
                dominantColorHex = newHex
            } else {
                // Edit ROW 2 (Palette List)
                if (!currentPalette.contains(newHex)) {
                    currentPalette.add(0, newHex)
                }
                activeTertiaryColorHex = newHex
            }
            showColorPicker = false
        }

        if (showColorPicker) {
            ColorPickerDialog(
                onDismiss = { showColorPicker = false },
                onColorSelected = onNewColorPicked
            )
        }

        Dialog(
            onDismissRequest = onDismissRequest,
            // interestingly cant use modifier to change width for dialog, it has default sizes and
            // i guess i override it here
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            // The main container of the dialog content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    // --- Header Section ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scan Complete",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        // does nothing for now, might remove it later unless i can think of a functionality to be used here
                        IconButton(onClick = { /* Optional: Menu action */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More options",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    //Custom Row for Editable Item Type
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                // Using a slightly different surface tone for the row background
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Checkroom,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ITEM TYPE",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            BasicTextField(
                                value = currentItemType,
                                onValueChange = { currentItemType = it },
                                textStyle = TextStyle(
                                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    val displayColor = parseHexColor(dominantColorHex)

                    ScanResultRow(
                        label = "DOMINANT COLOR",
                        value = dominantColorHex.uppercase(),
                        onEditClick = {
                            pickerTarget = ColorPickerTarget.PRIMARY
                            showColorPicker = true
                        },
                        leadingIconContent = {
                            // Color Swatch Circle
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(displayColor)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tertiary Colors",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(currentPalette) { hexCode ->
                            val color = parseHexColor(hexCode)
                            // Compare against activeTertiaryColorHex
                            val isSelected = (hexCode == activeTertiaryColorHex)

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    // CHANGED: Updates activeTertiaryColorHex
                                    .clickable { activeTertiaryColorHex = hexCode },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (color.luminance() > 0.5f) Color.Black else Color.White
                                    )
                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    // CHANGED: Set target to TERTIARY and open picker
                                    .clickable {
                                        pickerTarget = ColorPickerTarget.TERTIARY
                                        showColorPicker = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Color",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        val canDelete = currentPalette.size > 1

                        item {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (canDelete) MaterialTheme.colorScheme.errorContainer
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                    .clickable(enabled = canDelete) {
                                        currentPalette.remove(activeTertiaryColorHex)

                                        //Select the first remaining color so we don't have a ghost selection
                                        if (currentPalette.isNotEmpty()) {
                                            activeTertiaryColorHex = currentPalette.first()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete, // Requires import
                                    contentDescription = "Delete Selected Color",
                                    tint = if (canDelete) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Action Buttons Section ---
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Primary Action: Add to Closet (Room logic will handle this)
                        Button(
                       //Pass back the EDITED Item Name + Colors
                            onClick = { onAddToClosetClick(currentItemType, dominantColorHex, currentPalette.toList()) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Add to Closet")
                        }

                        // Secondary Action: Retake Photo
                        OutlinedButton(
                            onClick = onRetakePhotoClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Retake Photo")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Helper Composable for a single row of results (Icon/Swatch -> Text -> Edit button)
 * Honestly dont need this as a helper function anymore since the first row is just a editable text now
 * TODO refactor this helper into the main function
 */
@Composable
private fun ScanResultRow(
    label: String,
    value: String,
    onEditClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    leadingIconContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Using a slightly different surface tone for the row background
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Container (Rounded Square background for the icon/swatch)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            leadingIconContent()
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row {
            // Only show Trash Can if a delete action was provided
            if (onDeleteClick != null) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Color",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Existing Edit Button
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// Parses "#RRGGBB" string to Compose Color
fun parseHexColor(hex: String): Color {
    return try {
        Color(hex.toColorInt())
    } catch (e: Exception) {
        Color.Gray // Fallback
    }
}

// Helper to calculate luminance (for deciding text color on top of background)
fun Color.luminance(): Float {
    return (0.299 * red + 0.587 * green + 0.114 * blue).toFloat()
}