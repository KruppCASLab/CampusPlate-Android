package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.ui.ReservationUiState
import edu.cwru.caslab.campusplate.ui.ReservationViewModel
import edu.cwru.caslab.campusplate.ui.components.GenericClickableCard
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReservationCard(
    modifier: Modifier = Modifier,
    reservation: Reservation,
    listing: Listing?,
    currentTime: Long,
    onClick: () -> Unit
) {
  GenericClickableCard(
    modifier = modifier,
    onClick = onClick
  ) { 

    val minutes = getMinutes( from = currentTime / 1000, to = reservation.timeExpired )
    
    Text( 
      text = listing?.title ?: "[Error, Please Reload]",
      style = MaterialTheme.typography.titleLarge
    )

    Text(
      text = "Expires in $minutes Minutes.",
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

private fun getMinutes(from: Long, to: Long): Int {
  return ( (to-from) / 60 )
    .toInt()
}

@Composable
fun ReservationScreen(
  modifier: Modifier = Modifier,
  reservationViewModel: ReservationViewModel = viewModel(),
  navHostController: NavHostController,
  email: String,
  credential: String
) {

  val uiState by reservationViewModel.uiState.collectAsState()   
  reservationViewModel.setAuthorization(email, credential)
  reservationViewModel.getListings()
  reservationViewModel.getReservations()
  
  LaunchedEffect(Unit) {
    while(true) {
        delay(15.seconds)
        reservationViewModel.updateTimeMillis()
    }
  } 

  if (uiState.selectedReservation == null) {

    Column(
      modifier = modifier
        .systemBarsPadding()
        .padding(top = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {  

      if (uiState.reservations?.isEmpty() ?: true) {
        //TODO: Nothing Here Icon
      }

      LazyColumn(
        Modifier.fillMaxSize()
        .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) { 

        items(uiState.reservations ?: emptyList()) { reservation ->
          ReservationCard(
            reservation = reservation,
            listing = uiState.listingIDMap?.get(reservation.listingId),
            currentTime = uiState.currentTimeMillis,
            onClick = {
              reservationViewModel.selectReservation(reservation)
            }
          )
        }
      }
    }

  } else {
    SelectedReservationScreen(
      modifier = modifier,
      reservationViewModel = reservationViewModel,
      uiState = uiState
    ) 
  }
 
}

@Composable
fun SelectedReservationScreen(
  modifier: Modifier = Modifier,
  reservationViewModel: ReservationViewModel = viewModel(),
  uiState: ReservationUiState
) {
  Column(
    modifier = modifier
      .systemBarsPadding()
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = uiState.selectedReservation?.code.toString(),
      style = MaterialTheme.typography.displayMedium
    ) 
    Text(
      modifier = modifier.padding(16.dp),
      text = "Use this code to pick up your reservation.",
      style = MaterialTheme.typography.titleMedium
    )
    Button(
      modifier = Modifier
        .fillMaxWidth(0.4f),
      onClick = { reservationViewModel.deselectReservation() },
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
}
