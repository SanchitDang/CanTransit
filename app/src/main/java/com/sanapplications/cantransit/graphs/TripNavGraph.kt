package com.sanapplications.cantransit.graphs

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import com.sanapplications.cantransit.screens.trip_screen.AvailableTripsScreen
import com.sanapplications.cantransit.screens.home_screen.HomeScreen
import com.sanapplications.cantransit.screens.trip_screen.TripScreen
import com.sanapplications.cantransit.screens.settings_screen.SettingsScreen
import com.sanapplications.cantransit.screens.bottom_navigation.BottomBarItem
import com.sanapplications.cantransit.screens.trip_screen.TripDetailsScreen

@Composable
fun TripNavGraph(navController: NavHostController, sharedPreferences: SharedPreferences) {
    NavHost(
        navController = navController,
        startDestination = BottomBarItem.Home.route
    ) {
        composable(route = BottomBarItem.Home.route) {
            HomeScreen(navController, sharedPreferences = sharedPreferences)
        }
        composable(route = BottomBarItem.Location.route) {
            TripScreen(navController, sharedPreferences = sharedPreferences)
        }
        composable(route = BottomBarItem.Settings.route) {
            SettingsScreen(navController, sharedPreferences = sharedPreferences)
        }
        locationTransitNavGraph(navController, sharedPreferences = sharedPreferences)
    }
}

fun NavGraphBuilder.locationTransitNavGraph(navController: NavHostController, sharedPreferences: SharedPreferences) {
    navigation(
        route = RootGraph.DETAILS,
        startDestination = TripRoutes.AvailableTrips.route
    ) {
        composable(
            route = TripRoutes.AvailableTrips.route,
            arguments = listOf(
                navArgument("origin") { type = NavType.StringType },
                navArgument("destination") { type = NavType.StringType },
                navArgument("originLatLng") { type = NavType.StringType },
                navArgument("destinationLatLng") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val origin = backStackEntry.arguments?.getString("origin") ?: ""
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val originLatLng = backStackEntry.arguments?.getString("originLatLng") ?: ""
            val destinationLatLng = backStackEntry.arguments?.getString("destinationLatLng") ?: ""
            AvailableTripsScreen(navController = navController, origin = origin, destination = destination, originLatLng = originLatLng, destinationLatLng = destinationLatLng)
        }

        composable(
            route = TripRoutes.TripDetails.route,
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonData = backStackEntry.arguments?.getString("data") ?: ""
            val routeResponse = Gson().fromJson(jsonData, RouteResponse::class.java)
            TripDetailsScreen(navController, routeResponse)
        }
    }
}

sealed class TripRoutes(val route: String) {
    data object AvailableTrips : TripRoutes(route = "available_trips/{origin}/{destination}/{originLatLng}/{destinationLatLng}")
    data object TripDetails : TripRoutes(route = "trip_detail/{data}")
}