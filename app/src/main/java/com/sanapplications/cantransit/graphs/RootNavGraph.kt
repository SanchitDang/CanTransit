package com.sanapplications.cantransit.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sanapplications.cantransit.screens.bottom_navigation.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = RootGraph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController)
        composable(route = RootGraph.TRIP) {
            MainScreen()
        }
    }
}

object RootGraph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val TRIP = "trip_graph"
    const val DETAILS = "details_graph"
}