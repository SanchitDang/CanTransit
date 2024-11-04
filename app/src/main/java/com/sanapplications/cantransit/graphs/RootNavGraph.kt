package com.sanapplications.cantransit.graphs

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sanapplications.cantransit.screens.bottom_navigation.MainScreen
import com.sanapplications.cantransit.screens.settings_screen.SettingsViewModel

@Composable
fun RootNavigationGraph(navController: NavHostController, sharedPreferences: SharedPreferences, settingsViewModel: SettingsViewModel, startDestination: String) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination
    ) {
        authNavGraph(navController = navController, settingsViewModel = settingsViewModel)
        composable(route = RootGraph.TRIP) {
            MainScreen(sharedPreferences = sharedPreferences)
        }
    }
}

object RootGraph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val TRIP = "trip_graph"
    const val DETAILS = "details_graph"
}