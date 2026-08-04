package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.ui.LoginUiState
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import edu.cwru.caslab.campusplate.ui.State
import edu.cwru.caslab.campusplate.ui.components.Throbber

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
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
      Column(
          modifier = modifier.
            fillMaxSize()
            .imePadding(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
      ) {

          val focusManager = LocalFocusManager.current

          Image(
            modifier = Modifier.padding(bottom = 8.dp),
            painter = painterResource(R.drawable.playstore_transparent),
            contentScale = ContentScale.Crop,
            contentDescription = null
          )

          Text(
              text = "Welcome!",
              style = MaterialTheme.typography.displaySmall,
              fontWeight = FontWeight.Bold
          )
          Text(
              text = "Enter your school email to login.",
              style = MaterialTheme.typography.titleLarge,
          )
          TextField(
              modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 32.dp),
              value = uiState.loginFieldValue,
              readOnly = uiState.state == State.Loading,
              singleLine = true,
              onValueChange = { loginViewModel.updateLoginField(it) },
              label = { Text(text = stringResource(R.string.school_email_field_label)) },
              keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
              keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
              modifier = Modifier.fillMaxWidth(0.8f),
              onClick = {
                  loginViewModel.createUser(navController)
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
          ) {
              Text(text = stringResource(R.string.send_pin_button_text))
          }
      }
    }
}
