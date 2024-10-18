package com.sanapplications.cantransit

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Favourites : Routes("favourites")
    object Location : Routes("location")
    object Profile : Routes("profile")
    object Settings : Routes("settings")
    object AvailableRoutes : Routes("available_routes")
}