package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.ui.ManageFoodStopsViewModel
import edu.cwru.caslab.campusplate.ui.icons.menu

@Composable
fun ManageFoodStopsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    email: String,
    credential: String,
    manageFoodStopsViewModel: ManageFoodStopsViewModel = viewModel()
) {
    val uiState by manageFoodStopsViewModel.uiState.collectAsState()

    manageFoodStopsViewModel.setAuthorization(email = email, credential = credential)
    manageFoodStopsViewModel.getManagedFoodStops()

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Create Listing",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
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
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier.fillMaxWidth(0.4f),
                onClick = { manageFoodStopsViewModel.onBackInteract(navHostController) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Back")
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.8f)
                .weight(1f, false),
            onClick = { manageFoodStopsViewModel.createListing(navHostController) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Create Listing")
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
