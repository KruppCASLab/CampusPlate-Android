package edu.cwru.caslab.campusplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.cwru.caslab.campusplate.ui.ActiveScreen
import edu.cwru.caslab.campusplate.ui.ListingViewModel
import edu.cwru.caslab.campusplate.ui.LoginUiState
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import edu.cwru.caslab.campusplate.ui.screens.ListingCard
import edu.cwru.caslab.campusplate.ui.screens.ListingScreen
import edu.cwru.caslab.campusplate.ui.theme.CampusPlateTheme
import kotlinx.coroutines.launch

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
                  CampusPlateLoginScreen()
                }
            }
        }
    }
}

@Composable
fun CampusPlateLoginScreen(
  modifier: Modifier = Modifier,
  loginViewModel: LoginViewModel = viewModel(),
  navController: NavHostController = rememberNavController()
) {
  val uiState by loginViewModel.uiState.collectAsState()
  NavHost(
    navController = navController,
    startDestination = ActiveScreen.Login.name,
    modifier = Modifier
  ) {
    composable(route = ActiveScreen.Login.name) { 
      CampusPlateLogin(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Pin.name) { 
      CampusPlatePin(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Listing.name) { 
      ListingScreen(modifier = modifier, navController = navController, email = uiState.activeEmail, credential = uiState.credential)
    }
  }
}

@Composable
fun CampusPlateLogin(
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
            text = stringResource(R.string.campus_plate_login),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(64.dp))
        TextField(
            value = uiState.loginFieldValue,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            onValueChange = { loginViewModel.updateLoginField(it) },
            label = { Text(text = stringResource(R.string.school_email_field_label)) },
            keyboardOptions = KeyboardOptions.Default
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                loginViewModel.createUser(navController)
                navController.navigate(ActiveScreen.Pin.name)
            }
        ) {
            Text(text = stringResource(R.string.send_pin_button_text))
        }
    }
}

@Composable
fun CampusPlatePin( 
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
    OtpTextField(
      otpText = uiState.pinFieldValue,
      onOtpTextChange = { value, otpInputFilled ->
        loginViewModel.updatePinField(value = value, filled = otpInputFilled, navController = navController)
      }
    ) 
  }
}



@Preview(showBackground = true)
@Composable
fun CampusPlatePreview() {
    CampusPlateTheme {
        //CampusPlateLogin()
    }
}
