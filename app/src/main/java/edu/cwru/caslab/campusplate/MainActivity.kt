package edu.cwru.caslab.campusplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import edu.cwru.caslab.campusplate.ui.PinUiState
import edu.cwru.caslab.campusplate.ui.PinViewModel
import edu.cwru.caslab.campusplate.ui.theme.CampusPlateTheme

enum class PinScreen {
  Email,
  Pin
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusPlateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> 
                  //CampusPlateLogin(Modifier.padding(innerPadding))
                  CampusPlatePin(email = "kxm897@case.edu")
                }
            }
        }
    }
}

@Composable
fun CampusPlateLogin(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {
    val loginUiState by loginViewModel.uiState.collectAsState()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.campus_plate_login),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(64.dp))
        TextField(
            value = loginViewModel.loginFieldValue,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            onValueChange = { loginViewModel.updateLoginField(it) },
            label = { Text(text = stringResource(R.string.school_email_field_label)) },
            keyboardOptions = KeyboardOptions.Default
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                loginViewModel.createUser()
            }
        ) {
            Text(text = stringResource(R.string.send_pin_button_text))
        }
    }
}

@Composable
fun CampusPlatePin( 
    modifier: Modifier = Modifier,
    pinViewModel: PinViewModel = viewModel(),
    email: String
) {
  val pinUiState by pinViewModel.uiState.collectAsState()
  var otpText by remember { mutableStateOf("") }
  Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
  ) { 
    OtpTextField(
      otpText = pinViewModel.pinFieldValue,
      onOtpTextChange = { value, otpInputFilled ->
        pinViewModel.updatePinField(value = value, filled = otpInputFilled, email = email)
        //otpText = value
        //if (otpInputFilled) (pinViewModel.validatePin(url = "users/${email}"))
      }
    ) 
  }
}

@Preview(showBackground = true)
@Composable
fun CampusPlatePreview() {
    CampusPlateTheme {
        CampusPlateLogin()
    }
}
