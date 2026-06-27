package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.cwru.caslab.campusplate.ui.ListingUiState
import edu.cwru.caslab.campusplate.ui.ListingViewModel

@Composable
fun ListingDetailScreen(
  modifier: Modifier = Modifier,
  listingViewModel: ListingViewModel = viewModel(),
  uiState: ListingUiState,
) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Text(
          text = uiState.selectedListing?.title ?: "",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = uiState.selectedListing?.description ?: "",
          style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = "${uiState.selectedListing?.quantityRemaining} Remaining at ${
            uiState.foodStopIDMap?.get(uiState.selectedListing?.foodStopId)?.name}", 
          style = MaterialTheme.typography.titleSmall
        )
        Button(
            modifier = Modifier
              .fillMaxWidth(0.4f),
            onClick = { listingViewModel.deselectListing() },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
          Text(
            text = "Back" 
          )
        }
      }

        Button(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .fillMaxWidth(0.8f)
              .weight(1f, false),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
          Text(
            text = "Begin Reservation" 
          )
        }
      
    }  
}
