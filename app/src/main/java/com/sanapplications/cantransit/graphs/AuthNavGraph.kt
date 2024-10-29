package com.sanapplications.cantransit.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sanapplications.cantransit.screens.auth_screen.StartScreen
import com.sanapplications.cantransit.screens.auth_screen.LoginScreen
import com.sanapplications.cantransit.screens.auth_screen.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = RootGraph.AUTHENTICATION,
        startDestination = AuthRoutes.Start.route
    ) {
        composable(route = AuthRoutes.Start.route) {
            StartScreen(
                onClick = {
                    navController.popBackStack()
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
    object Start : AuthRoutes(route = "START")
    object SignUp : AuthRoutes(route = "SIGN_UP")
    object SignIn : AuthRoutes(route = "SIGN_IN")
    object Forgot : AuthRoutes(route = "FORGOT")
}