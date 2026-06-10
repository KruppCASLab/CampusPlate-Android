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
import edu.cwru.caslab.campusplate.model.Credential
import edu.cwru.caslab.campusplate.model.User
import kotlinx.coroutines.launch
import okio.IOException

data class LoginUiState (
  val loginFieldValue: String = ""
)

class LoginViewModel : ViewModel() {

  private var _uiState = MutableStateFlow(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
  var loginFieldValue by mutableStateOf("")

  init {
    clearField()
  }

  fun clearField() {
    _uiState.value = LoginUiState(loginFieldValue = "")
  }

  fun updateLoginField(value: String) {
    loginFieldValue = value
  }

  fun createUser() {
    viewModelScope.launch { 
      try {
        val user = User(userName = loginFieldValue, credential = Credential(label = "postman"))
        val listResult = CampusPlateApi.retrofitService.createUser(user)
      } catch (e: IOException) {
      }
    }
  }
}


