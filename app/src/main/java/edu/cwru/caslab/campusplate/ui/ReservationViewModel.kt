package edu.cwru.caslab.campusplate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.model.Reservation
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException
import kotlin.time.Clock

@OptIn(kotlin.time.ExperimentalTime::class)
data class ReservationUiState (
  val state: State = State.Idle,
  val authorization: String = "",
  val quantity: String = "",
  val reservations: List<Reservation>? = null,
  val selectedReservation: Reservation? = null,
  val listings: List<Listing>? = null,
  val listingIDMap: Map<Int, Listing>? = null,
  val listing: Listing? = null,
  val currentTimeMillis: Long = Clock.System.now()
    .toEpochMilliseconds()
)

class ReservationViewModel: ViewModel() {

  private var _uiState: MutableStateFlow<ReservationUiState> = MutableStateFlow(ReservationUiState())
  val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow() 

  @OptIn(kotlin.time.ExperimentalTime::class)
  fun updateTimeMillis() {
    _uiState.update { it.copy( 
      currentTimeMillis = Clock.System.now()
        .toEpochMilliseconds()
      ) }
  }

  private fun error() {
    _uiState.update { currentState -> currentState.copy( state = State.Error ) }
  }

  fun setAuthorization(email: String, credential: String) {
    _uiState.update { it.copy(
      authorization = Credentials.basic( username = email, password = credential )
    ) }
  }

  fun getListings() {
    viewModelScope.launch { 
      try {
        _uiState.update { it.copy( state = State.Loading ) }
        val listResult = CampusPlateApi.retrofitService.getListings(authorization = uiState.value.authorization)

        if (listResult.isSuccessful) {
          if (listResult.body()?.data != null) {
            _uiState.update { currentState -> currentState.copy(
              listings = listResult.body()?.data,
              listingIDMap = listResult.body()?.data?.associateBy({ it.listingId }, { it } ),
              state = State.Success
            ) }
          } else error()
        } else error()

      } catch (e: IOException) {
        error()
      }
    } 
  }

  fun getReservations() {
    viewModelScope.launch { 
      try {
        _uiState.update { it.copy( state = State.Loading ) }
        val listResult = CampusPlateApi.retrofitService.getReservations(authorization = uiState.value.authorization)

        if (listResult.isSuccessful) {
          if (listResult.body()?.data != null) {
            _uiState.update { currentState -> currentState.copy(
              reservations = listResult.body()?.data,
              state = State.Success
            ) }
          } else error()
        } else error()

      } catch (e: IOException) {
        error()
      }
    } 
  }

  fun onBackInteract(navController: NavHostController) {
    navController.navigate(ActiveScreen.Listing.name)
  }

  fun selectReservation(reservation: Reservation) {
    if ( uiState.value.reservations?.contains(reservation) ?: false ) {
      _uiState.update { it.copy( selectedReservation = reservation ) }
    }
  }

  fun deselectReservation() {
    _uiState.update { it.copy( selectedReservation = null ) }
  }

}

