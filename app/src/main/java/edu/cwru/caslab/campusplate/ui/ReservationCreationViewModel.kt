package edu.cwru.caslab.campusplate.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.model.ReservationRequest
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials

data class ReservationCreationUiState(
  override val state: State = State.Idle,
  val authorization: String = "",
  val quantityFieldValue: String = "1",
  val reservation: Reservation? = null,
  val listing: Listing? = null,
  val reservationMenuExpanded: Boolean = false
): UiStateCommon()

class ReservationCreationViewModel: ViewModelCommon<ReservationCreationUiState>(
  defaultState = ReservationCreationUiState()
) {

  fun setQuantityFieldValue(value: String) {
    if (value.isDigitsOnly()) {
      _uiState.update { it.copy(
        quantityFieldValue = value
      ) }
    }
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

  fun createReservation(navController: NavHostController) { 

      viewModelScope.launch { 

        val reservationRequest = ReservationRequest(
            listingId = uiState.value.listing?.listingId ?: -1,
            quanitity = uiState.value.quantityFieldValue.toInt()
        )

        val reservation = commonApiCall(
          onSuccess = { 
            navController.navigate(ActiveScreen.Listing.name)
            it.copy(state = State.Success)
          },
          onLoad = { it.copy(state = State.Loading) },
          onError = { it.copy(state = State.Error) },
          apiCall = { 
            CampusPlateApi.retrofitService.createReservation(
              authorization = uiState.value.authorization,
              reservationRequest = reservationRequest
            )
          } 
        )

        _uiState.update{ it.copy( reservation = reservation ) }
      } 

  }

}
