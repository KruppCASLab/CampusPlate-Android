package edu.cwru.caslab.campusplate.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import edu.cwru.caslab.campusplate.CampusPlateApp
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.model.Credential
import edu.cwru.caslab.campusplate.model.Pin
import edu.cwru.caslab.campusplate.model.User
import edu.cwru.caslab.campusplate.repository.CampusPlateApiRepository
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okio.IOException
import java.security.GeneralSecurityException

enum class State { Idle, Error, Loading, Success }
enum class ActiveScreen(@StringRes val title: Int) {
  Splash(title = R.string.splash_screen),
  Login(title = R.string.login_screen), 
  Pin(title = R.string.pin_screen),
  Listing(title = R.string.listing_screen),
  ReservationCreation(title = R.string.reservation_creation_screen),
  Reservation(title = R.string.reservation_screen),
  ManageFoodStops(title = R.string.manage_food_stops_screen),
  Camera(title = R.string.camera_screen)
}


data class LoginUiState (
  override val state: State = State.Idle,
  val activeScreen: ActiveScreen = ActiveScreen.Splash,
  val loginFieldValue: String = "",
  val pinFieldValue: String = "",
  val activeEmail: String = "",
  val credential: String = ""
): UiStateCommon()

class LoginViewModel(
  private val credentialRepository: StoredCredentialRepository,
  private val apiRepository: CampusPlateApiRepository
) : ViewModelCommon<LoginUiState>(
  defaultState = LoginUiState()
) {

  companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CampusPlateApp
                LoginViewModel(
                    credentialRepository = app.appContainer.storedCredentialRepository,
                    apiRepository = app.appContainer.apiRepository
                )
            }
        }
  }

  init {
    resetScreen()
    viewModelScope.launch {
      credentialRepository.storedCredential.collect { stored ->
        if (!stored.email.isNullOrBlank() && !stored.credential.isNullOrBlank()) {

          if (apiRepository.getFoodStops( // Check if Credentials are Valid
            email = stored.email,
            authorization = Credentials.basic(username = stored.email, password = stored.credential)
          ).isSuccessful) {

            _uiState.update { it.copy(
              activeEmail = stored.email,
              credential = stored.credential,
              activeScreen = ActiveScreen.Listing,
              state = State.Success
            ) }
        
          } else {
            _uiState.update { it.copy(
              credential = "",
              activeScreen = ActiveScreen.Login
            ) }
          }

        } else {
          _uiState.update { it.copy(
            credential = "",
            activeScreen = ActiveScreen.Login
          ) }
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
        val listResult = apiRepository.createUser(user)
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
      } catch (e: GeneralSecurityException) {
        error()
      } catch (e: IllegalArgumentException) {
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
        _uiState.update { currentState -> currentState.copy( state = State.Loading ) }
        val pin = Pin(pin = _uiState.value.pinFieldValue)
        val listResult = apiRepository.validatePin(email = uiState.value.activeEmail, id = uiState.value.activeEmail, pin = pin)
        if (listResult.isSuccessful) {
          if (listResult.body()?.status == 0) {
            val guid = listResult.body()?.data?.GUID

            if (guid != null) {
              credentialRepository.saveCredential(uiState.value.activeEmail, guid)       // email + GUID are persisted here
              _uiState.update { it.copy(
                credential = guid,
                state = State.Success,
                activeScreen = ActiveScreen.Listing
              ) }
              //navController.navigate(ActiveScreen.Listing.name) {
              //  popUpTo(navController.graph.id) { inclusive = true }
              //}
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


