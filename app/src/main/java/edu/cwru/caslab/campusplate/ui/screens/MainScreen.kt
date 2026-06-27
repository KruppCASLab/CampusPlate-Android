package edu.cwru.caslab.campusplate.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.cwru.caslab.campusplate.LoginViewModelFactory
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import edu.cwru.caslab.campusplate.ui.ActiveScreen
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel (
      factory = LoginViewModelFactory (
          StoredCredentialRepository(LocalContext.current.dataStore)
      )
  ),
    navController: NavHostController = rememberNavController()
) {
  val uiState by loginViewModel.uiState.collectAsState()
    LaunchedEffect(uiState.activeScreen) {
      if (uiState.activeScreen == ActiveScreen.Listing &&
        navController.currentDestination?.route != ActiveScreen.Listing.name)
      {
        navController.navigate(ActiveScreen.Listing.name) {
        popUpTo(ActiveScreen.Login.name) { inclusive = true }
      }
    }
  }
  NavHost(
    navController = navController,
    startDestination = ActiveScreen.Login.name,
    modifier = Modifier
  ) {
    composable(route = ActiveScreen.Login.name) { 
      LoginScreen(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Pin.name) { 
      PinScreen(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Listing.name) { 
      ListingScreen(modifier = modifier, navController = navController, email = uiState.activeEmail, credential = uiState.credential)
    } 
  }
}
