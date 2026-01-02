package com.example.cleanfit.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executor
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import android.graphics.Color as AndroidColor // Alias to avoid naming conflict
import androidx.core.graphics.toColorInt
import com.example.cleanfit.data.model.ScanResultData

@Composable
fun CameraScreen(
    onImageCaptured: (Boolean) -> Unit, // Callback when done
    onCloseClick: () -> Unit,           // Callback to go back
        viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    // Camera Permissions

     var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    // Binding camera to lifecycle
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            bindToLifecycle(lifecycleOwner)
        }
    }

    // Permission Setup

    if (hasCameraPermission) {
        CameraScreenUI(
            onCloseClick = onCloseClick,
            onCaptureClick = {

                takePhoto(context, cameraController){ bitmap, uri ->
                    viewModel.onPhotoCaptured(bitmap, uri)

//                    onImageCaptured(true)

                }
            },
            cameraPreview = {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            controller = cameraController
                        }
                    },
                    onRelease = { cameraController.unbind() }
                )
            }
        )
        if(uiState.showDialog && uiState.capturedUri != null){
            val result = uiState.analysisResult

            // Convert Hex String (e.g. "#FFFFFF") to Color Object
            val colorObj = try {
                Color(result.primaryColor.toColorInt())
            } catch (e: Exception) {
                Color.Gray // Fallback
            }

            ScanResultDialog(
                showDialog = true,
                data = ScanResultData(
                    itemType = result.category,
                    dominantColorName = result.primaryColor,
                    dominantColorHex = colorObj
                ),
                onDismissRequest = { viewModel.onDialogDismiss() },
                onEditItemTypeClick = { /* TODO */ },
                onEditColorClick = { /* TODO */ },
                onAddToClosetClick = {
                    viewModel.onDialogDismiss()
                    onImageCaptured(true) // Navigate away
                },
                onRetakePhotoClick = {
                    viewModel.onDialogDismiss()
                }
            )

        }

    } else {
        // Fallback Screen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text("Grant Camera Permission")
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap, Uri) -> Unit
) {
    val executor: Executor = ContextCompat.getMainExecutor(context)

    controller.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                // 1. Get the rotation (Cameras often capture 90° off)
                val rotationDegrees = image.imageInfo.rotationDegrees

                // 2. Convert to Bitmap and Fix Rotation
                val bitmap = image.toBitmap().rotate(rotationDegrees.toFloat())

                // 3. Save to a temporary file in Cache, maybe look into maybe saving to storage
                val file = File.createTempFile("scanned_clothing_", ".jpg", context.cacheDir)

                try {
                    val outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    // 4. Return both the Bitmap (for AI) and Uri (for Display)
                    onPhotoTaken(bitmap, Uri.fromFile(file))

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // ALWAYS close the image proxy
                    image.close()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        }
    )
}

// Extension helper to rotate the Bitmap
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

