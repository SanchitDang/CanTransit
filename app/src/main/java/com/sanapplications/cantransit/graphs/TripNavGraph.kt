package com.sanapplications.cantransit.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sanapplications.cantransit.screens.available_routes_screen.AvailableRoutesScreen
import com.sanapplications.cantransit.screens.favourites_screen.FavouritesScreen
import com.sanapplications.cantransit.screens.home_screen.HomeScreen
import com.sanapplications.cantransit.screens.trip_screen.TripScreen
import com.sanapplications.cantransit.screens.profile_screen.ProfileScreen
import com.sanapplications.cantransit.screens.settings_screen.SettingsScreen
import com.sanapplications.cantransit.screens.bottom_navigation.BottomBarItem

@Composable
fun TripNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.TRIP,
        startDestination = BottomBarItem.Home.route
    ) {
        composable(route = BottomBarItem.Home.route) {
           HomeScreen(navController)
        }
//        composable(route = BottomBarItem.Favourites.route) {
//            FavouritesScreen(navController)
//        }
        composable(route = BottomBarItem.Location.route) {
            TripScreen(navController)
        }
//        composable(route = BottomBarItem.Profile.route) {
//            ProfileScreen(navController)
//        }
        composable(route = BottomBarItem.Settings.route) {
            SettingsScreen(navController)
        }
        locationTransitNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.locationTransitNavGraph(navController: NavHostController) {
    navigation(
        route = RootGraph.DETAILS,
        startDestination = TripRoutes.AvailableTransitRoutes.route
    ) {
        composable(route = TripRoutes.AvailableTransitRoutes.route) {
            AvailableRoutesScreen(navController)
        }
    }
}

sealed class TripRoutes(val route: String) {
    object AvailableTransitRoutes : TripRoutes(route = "location_transit")
}