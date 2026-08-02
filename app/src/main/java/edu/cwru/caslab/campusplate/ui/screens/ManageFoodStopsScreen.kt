package edu.cwru.caslab.campusplate.ui.screens

import android.content.ContentValues
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.ui.ManageFoodStopsViewModel
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import edu.cwru.caslab.campusplate.ui.icons.menu

@Composable
fun ManageFoodStopsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    email: String,
    credential: String,
    onBackInteract: () -> Unit,
    sheetEnabledOverride: Boolean = false,
    manageFoodStopsViewModel: ManageFoodStopsViewModel = viewModel()
) {
    val uiState by manageFoodStopsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    manageFoodStopsViewModel.setAuthorization(email = email, credential = credential)
    manageFoodStopsViewModel.getManagedFoodStops()

    if (!uiState.takingPicture || sheetEnabledOverride) Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column { 

            TopNavigationBar(
              text = "Create Listing",
              onClick = { onBackInteract() }
            )

            AddImageBox(
              imageUri = uiState.capturedImageUri,
              onClick = { manageFoodStopsViewModel.onCameraInteract() }
            )

            OutlinedTextField(
                value = uiState.selectedFoodStop?.name ?: "",
                readOnly = true,
                label = { Text("Food Stop") },
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        menu,
                        contentDescription = "Select food stop",
                        modifier = Modifier.clickable { manageFoodStopsViewModel.toggleFoodStopMenuExpanded() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { manageFoodStopsViewModel.setFoodStopFieldSize(it) }
            )
            DropdownMenu(
                expanded = uiState.foodStopMenuExpanded,
                onDismissRequest = { manageFoodStopsViewModel.toggleFoodStopMenuExpanded() },
                modifier = Modifier.width(with(LocalDensity.current) { uiState.foodStopFieldSize.width.toDp() })
            ) {
                uiState.managedFoodStops?.forEach { foodStop ->
                    DropdownMenuItem(
                        text = { Text(foodStop.name) },
                        onClick = { manageFoodStopsViewModel.setSelectedFoodStop(foodStop) }
                    )
                }
            } 

            OutlinedTextField(
                value = uiState.titleField,
                label = { Text("Title") },
                onValueChange = { manageFoodStopsViewModel.updateTitleField(it) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.descriptionField,
                label = { Text("Description") },
                onValueChange = { manageFoodStopsViewModel.updateDescriptionField(it) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.quantityField,
                label = { Text("Quantity") },
                onValueChange = { manageFoodStopsViewModel.updateQuantityField(it) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.weightOuncesField,
                label = { Text("Weight (oz)") },
                onValueChange = { manageFoodStopsViewModel.updateWeightOuncesField(it) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.expirationDateField,
                label = { Text("Expiration Date (YYYY-MM-DD)") },
                onValueChange = { manageFoodStopsViewModel.updateExpirationDateField(it) },
                modifier = Modifier.fillMaxWidth()
            )  
            
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f)
                .weight(1f, false),
            onClick = { manageFoodStopsViewModel.createListing( onComplete = onBackInteract, context = context ) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Create Listing")
        }
    } else {
      CameraScreen(
        viewModel = manageFoodStopsViewModel,
        navController = navHostController
      )
    }
}

@Composable
fun AddImageBox(
    imageUri: Uri?,
    modifier: Modifier = Modifier,
    emptyHeight: Int = 180,
    imageHeight: Int = 220,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (imageUri != null) imageHeight.dp else emptyHeight.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = shape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit image",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add image",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Tap to add image",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Take a photo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ManageFoodStopsScreenPreview() {
//    CampusPlateTheme {
//        ManageFoodStopsScreen(
//            navHostController = rememberNavController(),
//            email = "",
//            credential = "",
//            manageFoodStopsViewModel = ManageFoodStopsViewModel()
//        )
//    }
//}
