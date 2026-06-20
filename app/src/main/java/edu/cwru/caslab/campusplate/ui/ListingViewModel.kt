package edu.cwru.caslab.campusplate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cwru.caslab.campusplate.model.Listing
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.cancel
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
  val listings: List<Listing>? = null
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

  //fun getImage() {
  //  viewModelScope.launch {
  //    try {
  //      val listResult = CampusPlateApi.retrofitService.getImage(authorization = getAuthorizaton())
  //    } catch (e: IOException) {

   //   }
    //}
  //}

}
 
