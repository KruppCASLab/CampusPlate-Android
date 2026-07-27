package edu.cwru.caslab.campusplate.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.model.FoodStop
import edu.cwru.caslab.campusplate.model.ListingCreationRequest
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException

data class ManageFoodStopsUiState(
    val state: State = State.Idle,
    val authorization: String = "",
    val managedFoodStops: List<FoodStop>? = null,
    val selectedFoodStop: FoodStop? = null,
    val foodStopMenuExpanded: Boolean = false,
    val titleField: String = "",
    val descriptionField: String = "",
    val quantityField: String = "1",
    val weightOuncesField: String = "",
    val expirationDateMillis: Long? = null
)

class ManageFoodStopsViewModel : ViewModel() {

    private var _uiState: MutableStateFlow<ManageFoodStopsUiState> = MutableStateFlow(ManageFoodStopsUiState())
    val uiState: StateFlow<ManageFoodStopsUiState> = _uiState.asStateFlow()

    fun setAuthorization(email: String, credential: String) {
        _uiState.update { it.copy(
            authorization = Credentials.basic(username = email, password = credential)
        ) }
    }

    fun getManagedFoodStops() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(state = State.Loading) }
                val result = CampusPlateApi.retrofitService.getManagedFoodStops(authorization = uiState.value.authorization)
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
        navController.navigate(ActiveScreen.Listing.name)
    }

    fun createListing(navController: NavHostController) {
        val state = uiState.value
        val foodStopId = state.selectedFoodStop?.foodStopId ?: return
        val quantity = state.quantityField.toIntOrNull() ?: return
        val weightOunces = state.weightOuncesField.toIntOrNull() ?: return
        val expirationDate = (state.expirationDateMillis ?: return) / 1000

        _uiState.update { it.copy(state = State.Loading) }
        viewModelScope.launch {
            try {
                val request = ListingCreationRequest(
                    foodStopId = foodStopId,
                    title = state.titleField,
                    description = state.descriptionField,
                    quantity = quantity,
                    weightOunces = weightOunces,
                    creationDate = System.currentTimeMillis() / 1000,
                    expirationDate = expirationDate,
                    image = null
                )
                val result = CampusPlateApi.retrofitService.createListing(
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
