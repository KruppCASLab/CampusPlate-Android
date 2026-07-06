package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
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
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.ui.ReservationCreationViewModel
import edu.cwru.caslab.campusplate.ui.components.TopNavigationBar
import edu.cwru.caslab.campusplate.ui.icons.menu

@Composable
fun ReservationCreationScreen(
  modifier: Modifier = Modifier,
  reservationCreationViewModel: ReservationCreationViewModel= viewModel(),
  email: String,
  credential: String,
  listing: Listing?,
  navController: NavHostController
) {

  val uiState by reservationCreationViewModel.uiState.collectAsState()

  LaunchedEffect(Unit) {    
    reservationCreationViewModel.setAuthorization(email = email, credential = credential)
    reservationCreationViewModel.setListing(listing)
  }
  
  Column (
      modifier = modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(start = 16.dp, end = 16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) { 

      Column {

        TopNavigationBar(
          text = "Create Reservation",
          onClick = {
            reservationCreationViewModel.onBackInteract(navController)
          }
        )

        Text(
          text = "Select the quantity you want to reserve",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = uiState.listing?.title ?: "",
          style = MaterialTheme.typography.titleMedium
        )
        val icon = if (uiState.reservationMenuExpanded) menu else menu //TODO: Change Icon
        
        OutlinedTextField(
          value = uiState.quantityFieldValue,
          readOnly = true,
          label = { Text(text = "Quantity") },
          onValueChange = { reservationCreationViewModel.setQuantityFieldValue(it) },
          trailingIcon = {
            Icon(icon, "Select",
            modifier = Modifier.clickable { reservationCreationViewModel.toggleReservationMenuExpanded() })
          },
          modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { reservationCreationViewModel.setQuantityFieldSize(it) }
        )
        DropdownMenu(
            expanded = uiState.reservationMenuExpanded,
            onDismissRequest = { reservationCreationViewModel.toggleReservationMenuExpanded() },
            modifier = Modifier
                .width(with(LocalDensity.current){uiState.quantityFieldSize.width.toDp()})
        ) {
            ( 1 .. ( uiState.listing?.quantity ?: 0 ) ).forEach {
                DropdownMenuItem(
                    text = { Text(text = it.toString()) },
                    onClick = {
                        reservationCreationViewModel.setQuantityFieldValue(it.toString())
                        reservationCreationViewModel.toggleReservationMenuExpanded()
                    }
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
              reservationCreationViewModel.createReservation(navController = navController)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
          Text(
            text = "Place Reservation" 
          )
        }
      
    }
}
