package edu.cwru.caslab.campusplate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException


data class ListingUiState (
  val email: String = "",
  val credential: String = "",
  val state: State = State.Idle,
  val listings: List<Listing>? = null,
  val foodStops: List<FoodStop>? = null,
  val selectedListing: Listing? = null,
  val foodStopIDMap: Map<Int, FoodStop>? = null,
  val menuExpanded: Boolean = false
)

class ListingViewModel: ViewModel() {

  private var _uiState: MutableStateFlow<ListingUiState> = MutableStateFlow(ListingUiState())
  val uiState: StateFlow<ListingUiState> = _uiState.asStateFlow()

  private fun getAuthorizaton(): String {
    return Credentials.basic(username = uiState.value.email, password = uiState.value.credential)
  }

  private fun error() {
    _uiState.update { currentState -> currentState.copy( state = State.Error ) }
  }

  fun setEmailCred(email: String, credential: String) {
    _uiState.update { currentState -> currentState.copy(
      email = email,
      credential = credential
    ) }
  }

  fun getListings() {
    viewModelScope.launch { 
      try {
        _uiState.update { currentState -> currentState.copy( state = State.Loading ) }
        val listResult = CampusPlateApi.retrofitService.getListings(authorization =  getAuthorizaton())

        if (listResult.isSuccessful) {
          if (listResult.body()?.data != null) {
            _uiState.update { currentState -> currentState.copy(
              listings = listResult.body()?.data,
              state = State.Success
            ) }
          } else error()
        } else error()

      } catch (e: IOException) {
        error()
      }
    } 
  }

  fun getFoodStops() { // TODO: Can Be Simplified
    viewModelScope.launch { 
      try {
        _uiState.update { currentState -> currentState.copy( state = State.Loading ) }
        val listResult = CampusPlateApi.retrofitService.getFoodStops(authorization =  getAuthorizaton())

        if (listResult.isSuccessful) {
          if (listResult.body()?.data != null) {
            _uiState.update { currentState -> currentState.copy(
              foodStops = listResult.body()?.data,
              foodStopIDMap = listResult.body()?.data?.associateBy({ it.foodStopId }, { it } ),
              state = State.Success
            ) }
          } else error()
        } else error()

      } catch (e: IOException) {
        error()
      }
    } 
  }

  fun selectListing(listing: Listing) {
    if (uiState.value.listings?.contains(listing) ?: false) {
      _uiState.update { it.copy( selectedListing = listing ) }
    }
  }

  fun deselectListing() {
    _uiState.update { it.copy( selectedListing = null ) }
  }

  fun menuButtonInteract(toggle: Boolean = true) {
    _uiState.update { it.copy( 
      menuExpanded = if (toggle) !uiState.value.menuExpanded
        else false
    ) }
  }

  fun reservationViewInteract(navHostController: NavHostController) {
    navHostController.navigate(ActiveScreen.Reservation.name)
  }

  fun reservationCreationInteract(navController: NavHostController) {
    navController.navigate(ActiveScreen.ReservationCreation.name)
  }

  fun manageFoodStopsInteract(navController: NavHostController) {
    navController.navigate(ActiveScreen.ManageFoodStops.name)
  }

  //fun getImage() {
  //  viewModelScope.launch {
  //    try {
  //      val listResult = CampusPlateApi.retrofitService.getImage(authorization = getAuthorizaton())
  //    } catch (e: IOException) {

   //   }
    //}
  //}

}
 
