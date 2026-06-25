package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.ui.LoginUiState
import edu.cwru.caslab.campusplate.ui.LoginViewModel

@Composable
fun PinScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    uiState: LoginUiState,
    navController: NavHostController
) {
  Column (
        modifier = modifier
          .fillMaxSize()
          .imePadding()
          .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
  ) {

    OtpTextField(
      modifier = modifier,
      otpText = uiState.pinFieldValue,
      onOtpTextChange = { value, otpInputFilled ->
        loginViewModel.updatePinField(value = value, filled = otpInputFilled, navController = navController)
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
