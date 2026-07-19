package edu.cwru.caslab.campusplate.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.ui.ListingUiState
import edu.cwru.caslab.campusplate.ui.ListingViewModel
import edu.cwru.caslab.campusplate.ui.State
import edu.cwru.caslab.campusplate.ui.components.Throbber
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import kotlin.io.encoding.Base64



@Composable
fun ListingDetailScreen(
  modifier: Modifier = Modifier,
  listingViewModel: ListingViewModel = viewModel(),
  uiState: ListingUiState,
  navController: NavHostController
) {

    val selectedListing = uiState.selectedListing
    val selectedFoodStop = uiState.foodStopIDMap?.get(selectedListing?.foodStopId)
    val image = uiState.listingImageMap[selectedListing?.listingId]

    Column(
      modifier = modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) { 

      Column { 

        Text(
          text = selectedListing?.title ?: "",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = selectedListing?.description ?: "",
          style = MaterialTheme.typography.titleMedium
        )
        Text(
          text = "${selectedListing?.quantityRemaining} Remaining at ${selectedFoodStop?.name}", 
          style = MaterialTheme.typography.titleSmall
        )

        
        if (image != null) {
          Image(
            bitmap = image,
            contentDescription = null
          ) 
        } else {
          if (uiState.imageState == State.Loading) Throbber()
          else {
            Image(
              painter = painterResource(id = R.drawable.spoony),
              contentDescription = null
            )
          }
        }
        
      }

        Button(
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .fillMaxWidth(0.8f)
              .weight(1f, false),
            onClick = {
              listingViewModel.reservationCreationInteract(navController = navController)
            },
            enabled = selectedFoodStop?.reservable == 1,
            colors = if (selectedFoodStop?.reservable == 1) {
              ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
            } else ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceDim,
                contentColor = MaterialTheme.colorScheme.onSurface
              )
        ) {
          Text(
            text = if (selectedFoodStop?.reservable == 1) "Begin Reservation" else "Unreservable"
          )
        }
      
    }  
}
