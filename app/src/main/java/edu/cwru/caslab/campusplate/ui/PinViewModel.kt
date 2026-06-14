package edu.cwru.caslab.campusplate.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import edu.cwru.caslab.campusplate.model.Pin
import kotlinx.coroutines.launch
import okio.IOException

data class PinUiState (
  val pinFieldValue: String = ""
)

class PinViewModel : ViewModel() {

  private var _uiState = MutableStateFlow(PinUiState())
  val uiState: StateFlow<PinUiState> = _uiState.asStateFlow()
  var pinFieldValue by mutableStateOf("")

  init {
    clearField()
  }

  fun clearField() {
    _uiState.value = PinUiState(pinFieldValue = "")
  }

  fun updatePinField(value: String, filled: Boolean, email: String) {
    pinFieldValue = value
    if (filled) (validatePin(email))
  }

  fun validatePin(email: String) {
    viewModelScope.launch { 
      try {
        val pin = Pin(pin = pinFieldValue)
        //val listResult = CampusPlateApi.retrofitService.validatePin(email = email, pin = pin)
      } catch (e: IOException) {
      }
    }
  }
}


