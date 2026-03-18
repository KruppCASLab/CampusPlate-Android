package com.example.modernization.user_interface.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.modernization.user_interface.screens.RegisterScreen
import com.example.modernization.user_interface.screens.RegistrationSuccessScreen
object Routes {
    const val REGISTER = "register"
    const val REGISTER_SUCCESS = "register_success"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.REGISTER   // or Routes.LOGIN for returning users
    ) {

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistrationSuccess = {
                    navController.navigate(Routes.REGISTER_SUCCESS) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {}
            )
        }

        composable(Routes.REGISTER_SUCCESS) {
            RegistrationSuccessScreen(
                onContinue = {}
            )
        }

    }
}




