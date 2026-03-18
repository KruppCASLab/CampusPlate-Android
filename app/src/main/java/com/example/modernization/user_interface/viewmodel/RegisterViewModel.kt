package com.example.modernization.user_interface.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modernization.model.requests.AuthStartRequest
import com.example.modernization.model.requests.ConfirmPinRequest
import com.example.modernization.model.requests.CreateUserRequest
import com.example.modernization.model.types.UserCredential
import com.example.modernization.network.CampusPlateApi
import com.example.modernization.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class PinSent(val email: String) : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel : ViewModel() {

    private val api: CampusPlateApi = RetrofitClient.instance

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state

    private var pendingEmail: String = ""

    /**
     * Step 1: Create account + send PIN email
     */
    fun registerAndSendPin(userName: String, email: String) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                // Create user with email credential
                val credential = UserCredential(type = 0, label = email)
                val createReq = CreateUserRequest(userName = userName, credential = credential)
                val createResp = api.createUser(createReq)

                if (!createResp.isSuccessful || createResp.body()?.error != null) {
                    val errMsg = createResp.body()?.error ?: "Failed to create account"
                    _state.value = RegisterState.Error(errMsg)
                    return@launch
                }

                // Send PIN email
                val authReq = AuthStartRequest(key = "email", email = email, pin = "")
                // emailPin uses a dynamic URL — adjust base path in your RetrofitClient if needed
                val pinResp = api.emailPin(authReq)

                if (!pinResp.isSuccessful) {
                    _state.value = RegisterState.Error("Account created but failed to send PIN")
                    return@launch
                }

                pendingEmail = email
                _state.value = RegisterState.PinSent(email)

            } catch (e: Exception) {
                _state.value = RegisterState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Step 2: Confirm PIN to validate account
     */
    fun confirmPin(pin: String) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val resp = api.confirmPin(
                    email = pendingEmail,
                    request = ConfirmPinRequest(pin = pin.toInt())
                )
                if (resp.isSuccessful && resp.body()?.error == null) {
                    _state.value = RegisterState.Success
                } else {
                    val errMsg = resp.body()?.error ?: "Invalid PIN"
                    _state.value = RegisterState.Error(errMsg)
                }
            } catch (e: Exception) {
                _state.value = RegisterState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _state.value = RegisterState.Idle
    }
}