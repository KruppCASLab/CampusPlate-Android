package edu.cwru.caslab.campusplate.ui.screens

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onImageCaptureReady: (ImageCapture?) -> Unit,
    onLoadingChanged: (Boolean) -> Unit = {},
    onError: (Throwable) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    fun unbindCamera() {
        cameraProvider?.unbindAll()
        onImageCaptureReady(null)
        onLoadingChanged(false)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                unbindCamera()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            unbindCamera()
        }
    }

    if (isActive) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                onLoadingChanged(true)

                PreviewView(ctx).also { pv ->
                    previewView = pv

                    val future = ProcessCameraProvider.getInstance(ctx)
                    future.addListener({
                        try {
                            val provider = future.get()
                            cameraProvider = provider

                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = pv.surfaceProvider
                            }

                            val imageCapture = ImageCapture.Builder().build()
                            onImageCaptureReady(imageCapture)

                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageCapture
                            )

                            onLoadingChanged(false)
                        } catch (e: Exception) {
                            onLoadingChanged(false)
                            onImageCaptureReady(null)
                            onError(e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            }
        )
    } else {
        LaunchedEffect(Unit) {
            unbindCamera()
        }
    }
}
