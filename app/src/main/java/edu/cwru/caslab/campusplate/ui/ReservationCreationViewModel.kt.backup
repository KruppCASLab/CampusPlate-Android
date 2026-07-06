package edu.cwru.caslab.campusplate.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.model.ReservationRequest
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException

data class ReservationCreationUiState (
  val state: State = State.Idle,
  val authorization: String = "",
  val quantityFieldValue: String = "1",
  val reservation: Reservation? = null,
  val listing: Listing? = null,
  val reservationMenuExpanded: Boolean = false,
  val quantityFieldSize: Size = Size.Zero
)

class ReservationCreationViewModel: ViewModel() {

  private var _uiState: MutableStateFlow<ReservationCreationUiState> = MutableStateFlow(ReservationCreationUiState())
  val uiState: StateFlow<ReservationCreationUiState> = _uiState.asStateFlow() 

  fun setQuantityFieldValue(value: String) {
    if (value.isDigitsOnly()) {
      _uiState.update { it.copy(
        quantityFieldValue = value
      ) }
    }
  }

  fun setQuantityFieldSize(coordinates: LayoutCoordinates) {
    _uiState.update { it.copy(
        quantityFieldSize = coordinates.size.toSize()
      ) }
  }

  fun setListing(listing: Listing?) {
    _uiState.update { it.copy(
        listing = listing
      ) }
  }

  fun setAuthorization(email: String, credential: String) {
    _uiState.update { it.copy(
      authorization = Credentials.basic( username = email, password = credential )
    ) }
  }

  fun toggleReservationMenuExpanded() {
    _uiState.update { it.copy(
        reservationMenuExpanded = !uiState.value.reservationMenuExpanded
    ) }
  }

  fun onBackInteract(navController: NavHostController) {
    navController.navigate(ActiveScreen.Listing.name)
  }

  private fun error() {
    _uiState.update { currentState -> currentState.copy( state = State.Error ) }
  }

  fun createReservation(navController: NavHostController) {
    _uiState.update { it.copy(state = State.Loading) }
    viewModelScope.launch { 
      try {
        val reservationRequest = ReservationRequest(
          listingId = uiState.value.listing?.listingId ?: -1,
          quanitity = uiState.value.quantityFieldValue.toInt()
        )
        val listResult = CampusPlateApi.retrofitService.createReservation(authorization = uiState.value.authorization, reservationRequest = reservationRequest)
        if (listResult.isSuccessful) {
          if (listResult.body()?.status == 0) {
            val reservation = listResult.body()?.data
            if (reservation != null) {
              _uiState.update { it.copy(reservation = reservation, state = State.Success) }
              navController.navigate(ActiveScreen.Reservation.name)
            } else {
              error()
            }
          } else error()
        } else error()
      } catch (e: IOException) {
        error()
      }
    }
  }

}
