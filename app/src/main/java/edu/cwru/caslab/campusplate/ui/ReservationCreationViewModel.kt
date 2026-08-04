package edu.cwru.caslab.campusplate.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.CampusPlateApp
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.model.ReservationRequest
import edu.cwru.caslab.campusplate.repository.CampusPlateApiRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials

data class ReservationCreationUiState(
  override val state: State = State.Idle,
  val authorization: String = "",
  val quantityFieldValue: String = "1",
  val reservation: Reservation? = null,
  val email: String = "",
  val listing: Listing? = null,
  val reservationMenuExpanded: Boolean = false
): UiStateCommon()

class ReservationCreationViewModel(
  private val apiRepository: CampusPlateApiRepository
): ViewModelCommon<ReservationCreationUiState>(
  defaultState = ReservationCreationUiState()
) {

  companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CampusPlateApp
                ReservationCreationViewModel(
                    apiRepository = app.appContainer.apiRepository
                )
            }
        }
  }

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
      email = email,
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
            apiRepository.createReservation(
              email = uiState.value.email, 
              authorization = uiState.value.authorization,
              reservationRequest = reservationRequest
            )
          } 
        )

        _uiState.update{ it.copy( reservation = reservation ) }
      } 

  }

}
