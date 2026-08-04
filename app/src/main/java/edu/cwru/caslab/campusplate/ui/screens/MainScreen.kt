package edu.cwru.caslab.campusplate.ui.screens

import android.content.Context
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
import edu.cwru.caslab.campusplate.repository.StoredCredentialRepository
import edu.cwru.caslab.campusplate.security.KeystoreCryptographer
import edu.cwru.caslab.campusplate.ui.ActiveScreen
import edu.cwru.caslab.campusplate.ui.LoginViewModel
import androidx.compose.runtime.getValue
import edu.cwru.caslab.campusplate.ui.ListingViewModel
import edu.cwru.caslab.campusplate.ui.ManageFoodStopsViewModel

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel ( factory = LoginViewModel.Factory ),
    listingViewModel: ListingViewModel = viewModel( factory = ListingViewModel.Factory ),
    navController: NavHostController = rememberNavController()
) {
  val uiState by loginViewModel.uiState.collectAsState()
    LaunchedEffect(uiState.activeScreen) {
      if (uiState.activeScreen == ActiveScreen.Listing &&
        navController.currentDestination?.route != ActiveScreen.Listing.name)
      {
        navController.navigate(ActiveScreen.Listing.name) {
          popUpTo(navController.graph.id) { inclusive = true }
        }
      } else if (uiState.activeScreen == ActiveScreen.Login &&
        navController.currentDestination?.route == ActiveScreen.Splash.name
      )
      {    
        navController.navigate(ActiveScreen.Login.name) {
          popUpTo(ActiveScreen.Splash.name) { inclusive = true }
        }
      }
    }

  val manageFoodStopsViewModel: ManageFoodStopsViewModel = viewModel(factory = ManageFoodStopsViewModel.Factory)

  val listingUiState by listingViewModel.uiState.collectAsState()
  NavHost(
    navController = navController,
    startDestination = ActiveScreen.Splash.name,
    modifier = Modifier
  ) {
    composable(route = ActiveScreen.Splash.name) {
      SplashScreen(modifier = modifier)
    }
    composable(route = ActiveScreen.Login.name) { 
      LoginScreen(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Pin.name) { 
      PinScreen(modifier = modifier, loginViewModel = loginViewModel, uiState = uiState, navController = navController)
    }
    composable(route = ActiveScreen.Listing.name) { 
      ListingScreen(modifier = modifier, listingViewModel = listingViewModel, navHostController = navController, email = uiState.activeEmail, credential = uiState.credential)
    } 
    composable(route = ActiveScreen.Reservation.name) {
      ReservationScreen(modifier = modifier, navHostController = navController, email = uiState.activeEmail, credential = uiState.credential)
    }
    composable(route = ActiveScreen.ReservationCreation.name) {
      ReservationCreationScreen(modifier = modifier, navController = navController, email = uiState.activeEmail, credential = uiState.credential, listing = listingUiState.selectedListing)
    }
    composable(route = ActiveScreen.Camera.name) {
      CameraScreen(viewModel = manageFoodStopsViewModel, navController = navController)
    }
  }
}
