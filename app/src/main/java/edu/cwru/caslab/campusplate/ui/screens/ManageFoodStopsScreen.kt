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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

private fun formatEpochMillis(millis: Long): String =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(dateFormatter)

@OptIn(ExperimentalMaterial3Api::class)
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

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.expirationDateMillis
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        manageFoodStopsViewModel.setExpirationDate(it)
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
 
            ExposedDropdownMenuBox(
                expanded = uiState.foodStopMenuExpanded,
                onExpandedChange = { manageFoodStopsViewModel.toggleFoodStopMenuExpanded() }
            ) {
                OutlinedTextField(
                    value = uiState.selectedFoodStop?.name ?: "",
                    readOnly = true,
                    label = { Text("Food Stop") },
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.foodStopMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = uiState.foodStopMenuExpanded,
                    onDismissRequest = { manageFoodStopsViewModel.toggleFoodStopMenuExpanded() }
                ) {
                    uiState.managedFoodStops?.forEach { foodStop ->
                        DropdownMenuItem(
                            text = { Text(foodStop.name) },
                            onClick = { manageFoodStopsViewModel.setSelectedFoodStop(foodStop) }
                        )
                    }
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
                value = uiState.expirationDateMillis?.let { formatEpochMillis(it) } ?: "",
                readOnly = true,
                label = { Text("Expiration Date") },
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
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
