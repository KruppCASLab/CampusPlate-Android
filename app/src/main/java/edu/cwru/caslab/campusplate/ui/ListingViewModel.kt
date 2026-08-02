package edu.cwru.caslab.campusplate.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
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
import kotlin.io.encoding.Base64

enum class SheetActiveView { Listing, FoodStop, ListingInfo, Reservation, Manage }

data class ListingUiState (
  val email: String = "",
  val credential: String = "",
  val state: State = State.Idle,
  val imageState: State = State.Idle,
  val listings: List<Listing>? = null,
  val foodStops: List<FoodStop>? = null,
  val selectedListing: Listing? = null,
  val selectedFoodStop: FoodStop? = null,
  val foodStopIDMap: Map<Int, FoodStop>? = null,
  val sheetActiveView: SheetActiveView = SheetActiveView.Listing,
  val listingImageMap: MutableMap<Int, ImageBitmap> = mutableMapOf<Int, ImageBitmap>(),
  val menuExpanded: Boolean = false
)

fun ByteArray.toImageBitmap() = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap() // Helper

fun String.decodeBase64ToByteArray(): ByteArray { // Helper
  val byteArray = encodeToByteArray()
  return Base64.decode(byteArray, 0, byteArray.size)
}

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

  fun selectFoodStop(foodstop: FoodStop?) {
    if (uiState.value.foodStops?.contains(foodstop) ?: false) {
      _uiState.update { it.copy( selectedFoodStop = foodstop ) }
    }
    if (foodstop!=null) 
      changeView(view = SheetActiveView.FoodStop)
    else
      changeView(view = SheetActiveView.Listing)
  }

  private fun changeView(view: SheetActiveView) {
    _uiState.update { it.copy( sheetActiveView = view ) }
  }

  fun toggleListingView() {
    if (uiState.value.sheetActiveView == SheetActiveView.Listing) { 
      _uiState.update { it.copy( sheetActiveView = SheetActiveView.Reservation ) }
    } else _uiState.update { it.copy( sheetActiveView = SheetActiveView.Listing ) }
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

  fun getListingImage(listing: Listing) {
    viewModelScope.launch { 
      try {
        _uiState.update { it.copy( imageState = State.Loading ) }
        val listResult = CampusPlateApi.retrofitService.getListingImage(authorization = getAuthorizaton(), id = listing.listingId.toString())
        if (listResult.isSuccessful) {
          if (listResult.body()?.data != null) {
            val imageString = listResult.body()?.data ?: ""
            if (imageString != "")
              uiState.value.listingImageMap[listing.listingId] = imageString.decodeBase64ToByteArray().toImageBitmap()
              _uiState.update { it.copy( imageState = State.Success ) }
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
    changeView(view = SheetActiveView.ListingInfo)
  }

  fun deselectListing() {
    _uiState.update { it.copy( selectedListing = null ) }
    changeView(view = SheetActiveView.Listing)
  }

  fun reservationCreationInteract(navController: NavHostController) {
    navController.navigate(ActiveScreen.ReservationCreation.name)
  }

  fun manageFoodStopsInteract(navController: NavHostController) {
    changeView(view = SheetActiveView.Manage)
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
 
