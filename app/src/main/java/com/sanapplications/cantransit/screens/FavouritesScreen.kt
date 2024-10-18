package com.sanapplications.cantransit.screens

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.sanapplications.cantransit.graphs.LocationTransit

@Composable
fun FavouritesScreen(navController: NavHostController) {
    Text(text = "Favourites Screen", modifier = Modifier.clickable{
        navController.navigate(LocationTransit.AvailableTransitRoutes.route)
    })
}