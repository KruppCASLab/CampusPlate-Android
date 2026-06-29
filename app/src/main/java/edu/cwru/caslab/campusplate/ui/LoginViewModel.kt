package edu.cwru.caslab.campusplate.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.network.CampusPlateApi
import edu.cwru.caslab.campusplate.model.Credential
import edu.cwru.caslab.campusplate.model.Pin
import edu.cwru.caslab.campusplate.model.User
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

enum class State { Idle, Error, Loading, Success }
enum class ActiveScreen(@StringRes val title: Int) {
  Login(title = R.string.login_screen), 
  Pin(title = R.string.pin_screen),
  Listing(title = R.string.listing_screen),
  ReservationCreation(title = R.string.reservation_creation_screen),
  Reservation(title = R.string.reservation_screen)
}


data class LoginUiState (
  val state: State = State.Idle,
  val activeScreen: ActiveScreen = ActiveScreen.Login,
  val loginFieldValue: String = "",
  val pinFieldValue: String = "",
  val activeEmail: String = "",
  val credential: String = ""
)

class LoginViewModel(
  private val repository: StoredCredentialRepository
) : ViewModel() {

  private var _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  init {
    resetScreen()
    viewModelScope.launch {
      repository.storedCredential.collect { stored ->
        if (!stored.email.isNullOrBlank() && !stored.credential.isNullOrBlank()) {
          _uiState.update { it.copy(
            activeEmail = stored.email,
            credential = stored.credential,
            activeScreen = ActiveScreen.Listing,
            state = State.Success
          ) }
        } else {
          _uiState.update { it.copy(credential = "") }
        }
      }
    }
  }

  fun resetScreen() {
    _uiState.value = LoginUiState()
  }

  fun updateLoginField(value: String) {
    if (_uiState.value.activeScreen == ActiveScreen.Login) {
      _uiState.update { currentState -> currentState.copy( loginFieldValue = value ) }
    }
  }

  fun createUser(navController: NavController) { //TODO: Add email validation
    viewModelScope.launch { 
      try {
        _uiState.update { currentState -> currentState.copy( state = State.Loading ) }
        val user = User(userName = uiState.value.loginFieldValue, credential = Credential(label = "postman"))
        val listResult = CampusPlateApi.retrofitService.createUser(user)
        if (listResult.isSuccessful) {
          if (listResult.body()?.status == 0 || listResult.body()?.status == 2) {
            _uiState.update { currentState -> currentState.copy(
              activeScreen = ActiveScreen.Pin,
              activeEmail = uiState.value.loginFieldValue,
              state = State.Idle
            ) }
            navController.navigate(ActiveScreen.Pin.name)
          } else error()
        } else error()
      } catch (e: IOException) {
        error()
      }
    }
  }

  fun getCensoredEmail(): String {
    val emailParts = uiState.value.activeEmail.split("@")
    val email = emailParts.first()
    val domain = emailParts.last()
    return "${ email.substring(0..1) }**@${ domain }"
  }

  fun updatePinField(value: String, filled: Boolean, navController: NavController) {
    if (_uiState.value.activeScreen == ActiveScreen.Pin) {
      _uiState.update { currentState -> currentState.copy( pinFieldValue = value ) }
    }
    if (filled) (validatePin(navController))
  }

  private fun error() {
    _uiState.update { currentState -> currentState.copy( state = State.Error ) }
  }

  fun validatePin(navController: NavController) {
    viewModelScope.launch { 
      try {
        val pin = Pin(pin = _uiState.value.pinFieldValue)
        val listResult = CampusPlateApi.retrofitService.validatePin(id = uiState.value.activeEmail, pin = pin)
        if (listResult.isSuccessful) {
          if (listResult.body()?.status == 0) {
            val guid = listResult.body()?.data?.GUID

            if (guid != null) {
              repository.saveCredential(uiState.value.activeEmail, guid)       // email + GUID are persisted here
              _uiState.update { it.copy(credential = guid, state = State.Success) }
              navController.navigate(ActiveScreen.Listing.name)
            } else {
              error()
            }
          } else error()
        } else error()
      } catch (e: IOException) {
        error()
      }
    }
  }
}


