package edu.cwru.caslab.campusplate.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.ui.ManageFoodStopsViewModel
import edu.cwru.caslab.campusplate.ui.components.Throbber
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar

fun createImageOutputOptions(context: Context): androidx.camera.core.ImageCapture.OutputFileOptions {
    val name = "IMG_${System.currentTimeMillis()}"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images")
        }
    }

    val contentResolver = context.contentResolver
    val imageCollection =
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    return androidx.camera.core.ImageCapture.OutputFileOptions
        .Builder(contentResolver, imageCollection, contentValues)
        .build()
}

@Composable
fun CameraScreen(
    viewModel: ManageFoodStopsViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    BackHandler { 
      viewModel.onCameraBackInteract()
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCameraLoading by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
        ) {

            TopNavigationBar(
              text = "Create Listing",
              onClick = { viewModel.onCameraBackInteract() }
            )

            if (hasCameraPermission) {
                

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onImageCaptureReady = { capture ->
                            imageCapture = capture
                        },
                        onLoadingChanged = { loading ->
                            isCameraLoading = loading
                        }
                    )

                    if (isCameraLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                              horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Throbber()
                                Text(
                                  text = "Please wait while the camera loads..."
                                )
                            }
                        }
                    }
                    
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val capture = imageCapture ?: return@Button
                        val outputOptions = createImageOutputOptions(context)

                        capture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    outputFileResults.savedUri?.let { uri ->
                                      viewModel.onImageCaptured(uri = uri)
                                    }
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.printStackTrace()
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCameraLoading
                ) {
                    Text("Take Picture")
                }

            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("This feature requires Camera Permissions")
                }
            }
        }
    }
}
