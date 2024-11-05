package com.sanapplications.cantransit

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Favourites : Routes("favourites")
    data object Location : Routes("location")
    data object Profile : Routes("profile")
    data object Settings : Routes("settings")
    data object AvailableRoutes : Routes("available_routes")
}