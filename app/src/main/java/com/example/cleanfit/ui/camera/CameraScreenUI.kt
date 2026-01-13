package com.example.cleanfit.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreenUI(
    onCloseClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onGalleryClick: () -> Unit,
    cameraPreview: @Composable () -> Unit // Pass the camera view in as a slot
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Full Screen Camera Feed
        cameraPreview()

        // 2. Top Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassyButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            GlassyButton(onClick = { /* Toggle Flash */ }) {
                Icon(Icons.Default.FlashOff, contentDescription = "Flash", tint = Color.White)
                // id have to pass flash state here to toggle it, will do it later cause its a hassle
            }
        }

        // 3. Bottom Capture Button

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly // Distribute space
        ) {

            // --- GALLERY BUTTON (Left) ---
            GlassyButton(onClick = onGalleryClick) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Gallery",
                    tint = Color.White
                )
            }

            // --- CAPTURE BUTTON (Center) ---
            CaptureButton(onClick = onCaptureClick)

            // --- EMPTY SPACER (Right) ---
            Spacer(modifier = Modifier.size(48.dp))
        }

//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 64.dp)
//        ) {
//            CaptureButton(onClick = onCaptureClick)
//        }
    }
}

// Helper Components
@Composable
fun GlassyButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CaptureButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .border(4.dp, Color.White, CircleShape)
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White, CircleShape)
        )
    }
}