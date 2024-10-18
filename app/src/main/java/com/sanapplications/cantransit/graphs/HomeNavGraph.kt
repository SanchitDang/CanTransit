package com.sanapplications.cantransit.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sanapplications.cantransit.screens.AvailableRoutesScreen
import com.sanapplications.cantransit.screens.FavouritesScreen
import com.sanapplications.cantransit.screens.HomeScreen
import com.sanapplications.cantransit.screens.LocationScreen
import com.sanapplications.cantransit.screens.ProfileScreen
import com.sanapplications.cantransit.screens.SettingsScreen
import com.sanapplications.cantransit.screens.bottom_navigation.BottomBarScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.HOME,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
           HomeScreen(navController)
        }
        composable(route = BottomBarScreen.Favourites.route) {
            FavouritesScreen(navController)
        }
        composable(route = BottomBarScreen.Location.route) {
            LocationScreen(navController)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(navController)
        }
        locationTransitNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.locationTransitNavGraph(navController: NavHostController) {
    navigation(
        route = RootGraph.DETAILS,
        startDestination = LocationTransit.AvailableTransitRoutes.route
    ) {
        composable(route = LocationTransit.AvailableTransitRoutes.route) {
            AvailableRoutesScreen(navController)
        }
    }
}

sealed class LocationTransit(val route: String) {
    object AvailableTransitRoutes : LocationTransit(route = "location_transit")
}