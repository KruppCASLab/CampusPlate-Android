package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.ui.LoginUiState
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import edu.cwru.caslab.campusplate.ui.State
import edu.cwru.caslab.campusplate.ui.components.Throbber

@Composable
fun PinScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    uiState: LoginUiState,
    navController: NavHostController
) {
  Box {
    Column(
        modifier = modifier
          .padding(16.dp)
          .systemBarsPadding()
          .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally 
      ) {
        if (uiState.state == State.Loading) Throbber()
      }
    Column (
          modifier = modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
    ) {


      val focusManager = LocalFocusManager.current

      OtpTextField(
        modifier = modifier,
        otpText = uiState.pinFieldValue,
        readOnly = uiState.state == State.Loading,
        onOtpTextChange = { value, otpInputFilled ->
          loginViewModel.updatePinField(value = value, filled = otpInputFilled, navController = navController)
          if (otpInputFilled) focusManager.clearFocus()
        }
      )
   
      Spacer(modifier = modifier.padding(top = 16.dp))

      Text(
        text = "We've sent a PIN to ${loginViewModel.getCensoredEmail()}.",
        style = MaterialTheme.typography.titleLarge
      )

      Text(
        text = "Check your email.",
        style = MaterialTheme.typography.titleMedium
      )
       
    }
  }
}
