package edu.cwru.caslab.campusplate.ui

import android.net.Uri
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.model.ListingCreationRequest
import edu.cwru.caslab.campusplate.network.OpenFoodFactsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException
import java.time.LocalDate
import java.time.ZoneOffset
import android.content.Context
import android.util.Base64
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.cwru.caslab.campusplate.CampusPlateApp
import edu.cwru.caslab.campusplate.repository.CampusPlateApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun uriToBase64(context: Context, uri: Uri): String {
    val bytes = context.contentResolver.openInputStream(uri)?.use { inputStream ->
        inputStream.readBytes()
    } ?: throw IllegalArgumentException("Could not read image from uri: $uri")

    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

data class ManageFoodStopsUiState(
    override val state: State = State.Idle,
    val authorization: String = "",
    val managedFoodStops: List<FoodStop>? = null,
    val selectedFoodStop: FoodStop? = null,
    val foodStopMenuExpanded: Boolean = false,
    val titleField: String = "",
    val descriptionField: String = "",
    val email: String = "",
    val quantityField: String = "1",
    val weightOuncesField: String = "",
    val expirationDateField: String = "",
    val capturedImageUri: Uri? = null,
    val takingPicture: Boolean = false,
    val expirationDateMillis: Long? = null
): UiStateCommon()

class ManageFoodStopsViewModel(
  private val apiRepository: CampusPlateApiRepository
) : ViewModelCommon<ManageFoodStopsUiState>(
  defaultState = ManageFoodStopsUiState()
) {
    
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CampusPlateApp
                ManageFoodStopsViewModel(
                    apiRepository = app.appContainer.apiRepository
                )
            }
        }
    }

    fun setAuthorization(email: String, credential: String) {
        _uiState.update { it.copy(
            email = email,
            authorization = Credentials.basic(username = email, password = credential)
        ) }
    } 

    fun onImageCaptured(uri: Uri) {
        _uiState.update { it.copy(
          capturedImageUri = uri
        ) }
        onCameraBackInteract()
    }

    fun clearImage() {
        _uiState.update { it.copy(
          capturedImageUri = null 
        ) }
    }

    fun onCameraInteract() {
      _uiState.update { it.copy( takingPicture = true ) }
    }

    fun onCameraBackInteract() {
      _uiState.update { it.copy( takingPicture = false ) } 
    }

    fun getManagedFoodStops() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(state = State.Loading) }
                val result = apiRepository.getManagedFoodStops(email = uiState.value.email, authorization = uiState.value.authorization)
                if (result.isSuccessful) {
                    if (result.body()?.data != null) {
                        _uiState.update { it.copy(
                            managedFoodStops = result.body()?.data,
                            state = State.Success
                        ) }
                    } else error()
                } else error()
            } catch (e: IOException) {
                error()
            }
        }
    }

    fun setSelectedFoodStop(foodStop: FoodStop) {
        _uiState.update { it.copy(selectedFoodStop = foodStop, foodStopMenuExpanded = false) }
    }

    fun toggleFoodStopMenuExpanded() {
        _uiState.update { it.copy(foodStopMenuExpanded = !uiState.value.foodStopMenuExpanded) }
    }

    fun updateTitleField(value: String) {
        _uiState.update { it.copy(titleField = value) }
    }

    fun updateDescriptionField(value: String) {
        _uiState.update { it.copy(descriptionField = value) }
    }

    fun updateQuantityField(value: String) {
        if (value.isDigitsOnly()) {
            _uiState.update { it.copy(quantityField = value) }
        }
    }

    fun updateWeightOuncesField(value: String) {
        if (value.isDigitsOnly()) {
            _uiState.update { it.copy(weightOuncesField = value) }
        }
    }

    fun setExpirationDate(millis: Long) {
        _uiState.update { it.copy(expirationDateMillis = millis) }
    }

    fun onBackInteract(navController: NavHostController) {
        _uiState.update { it.copy( takingPicture = false ) } 
        navController.navigate(ActiveScreen.Listing.name)
    }

    fun createListing(onComplete: () -> Unit, context: Context) {
        val state = uiState.value
        val foodStopId = state.selectedFoodStop?.foodStopId ?: return
        val quantity = state.quantityField.toIntOrNull() ?: return
        val weightOunces = state.weightOuncesField.toDoubleOrNull() ?: return
        val expirationDate = (state.expirationDateMillis ?: return) / 1000

        _uiState.update { it.copy(state = State.Loading) }
        viewModelScope.launch {
            try {

                val base64Image: String? = state.capturedImageUri?.let { withContext(Dispatchers.IO) {
                    uriToBase64(context, state.capturedImageUri) }
                }

                val request = ListingCreationRequest(
                    foodStopId = foodStopId,
                    title = state.titleField,
                    description = state.descriptionField,
                    quantity = quantity,
                    weightOunces = weightOunces,
                    creationDate = System.currentTimeMillis() / 1000,
                    expirationDate = expirationDate,
                    image = base64Image
                )
                val result = apiRepository.createListing(
                    email = uiState.value.email, 
                    authorization = state.authorization,
                    listingCreationRequest = request
                )
                if (result.isSuccessful) {
                    if (result.body()?.status == 0) {
                        _uiState.update { it.copy(
                            state = State.Success,
                            titleField = "",
                            descriptionField = "",
                            quantityField = "1",
                            weightOuncesField = "",
                            expirationDateMillis = null
                        ) }
                        clearImage()
                        onComplete()
                    } else error()
                } else error()
            } catch (e: IOException) {
                error()
            }
        }
    }

    fun autofillListing(barcodeString: String) {
      
      _uiState.update { it.copy(state = State.Loading) }
 
      viewModelScope.launch {
            try {
                _uiState.update { it.copy(state = State.Loading) }
                val result = OpenFoodFactsApi.retrofitService.getProduct(id = barcodeString)
                if (result.isSuccessful) {
                    if (result.body() != null && result.body()?.product != null) {
                        val product = result.body()?.product
                        _uiState.update { it.copy(
                            titleField = "${product?.brands}: ${product?.categories}",
                            descriptionField = "Allergens: ${ if (product?.allergens == "") "None" else product?.allergens ?: "None"}",
                            state = State.Success
                        ) }
                        product?.image_url?.let { url ->
                          _uiState.update { it.copy( capturedImageUri = Uri.parse(url) ) }
                        }
                    } else error()
                } else error()
            } catch (e: IOException) {
                error()
            }
        }

    }

    private fun error() {
        _uiState.update { currentState -> currentState.copy(state = State.Error) }
    }
}
