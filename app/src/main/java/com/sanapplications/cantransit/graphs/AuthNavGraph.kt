package com.sanapplications.cantransit.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sanapplications.cantransit.screens.auth_screen.StartScreen
import com.sanapplications.cantransit.screens.auth_screen.LoginScreen
import com.sanapplications.cantransit.screens.auth_screen.SignUpScreen
import com.sanapplications.cantransit.screens.settings_screen.SettingsViewModel

fun NavGraphBuilder.authNavGraph(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    navigation(
        route = RootGraph.AUTHENTICATION,
        startDestination = AuthRoutes.Start.route
    ) {
        composable(route = AuthRoutes.Start.route) {
            StartScreen(
                onClick = {
                    navController.popBackStack()
                    settingsViewModel.setFirstLaunchComplete()
                    navController.navigate(RootGraph.TRIP)
                },
                onSignUpClick = {
                    navController.navigate(AuthRoutes.SignUp.route)
                },
                onSignInClick = {
                    navController.navigate(AuthRoutes.SignIn.route)
                },
                onForgotClick = {
                    navController.navigate(AuthRoutes.Forgot.route)
                }
            )
        }
        composable(route = AuthRoutes.SignUp.route) {
            SignUpScreen() {}
        }
        composable(route = AuthRoutes.Forgot.route) {
            LoginScreen() {}
        }
    }
}

sealed class AuthRoutes(val route: String) {
    data object Start : AuthRoutes(route = "START")
    data object SignUp : AuthRoutes(route = "SIGN_UP")
    data object SignIn : AuthRoutes(route = "SIGN_IN")
    data object Forgot : AuthRoutes(route = "FORGOT")
}