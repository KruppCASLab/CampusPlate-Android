package edu.cwru.caslab.campusplate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import edu.cwru.caslab.campusplate.R
import edu.cwru.caslab.campusplate.ui.ActiveScreen
import edu.cwru.caslab.campusplate.ui.LoginUiState
import edu.cwru.caslab.campusplate.ui.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(),
    uiState: LoginUiState,
    navController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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
            singleLine = true,
            onValueChange = { loginViewModel.updateLoginField(it) },
            label = { Text(text = stringResource(R.string.school_email_field_label)) },
            keyboardOptions = KeyboardOptions.Default
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
