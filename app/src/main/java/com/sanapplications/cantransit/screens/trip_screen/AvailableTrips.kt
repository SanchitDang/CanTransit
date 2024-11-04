package com.sanapplications.cantransit.screens.trip_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.sanapplications.cantransit.graphs.TripRoutes
import com.sanapplications.cantransit.models.BusInfo
import com.sanapplications.cantransit.widgets.BusScheduleTable
import com.sanapplications.cantransit.widgets.PrimaryButton
import com.sanapplications.cantransit.widgets.TripView
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AvailableTripsScreen(
    navController: NavHostController?,
    origin: String,
    destination: String,
    originLatLng: String,
    destinationLatLng: String
) {

    val busList = remember { mutableStateListOf<BusInfo>() }

    var encodedPolylinePoints by remember { mutableStateOf("") }

    val context = LocalContext.current
    val routesViewModel: TripViewModel = viewModel()
    val routeState by routesViewModel.routeState.collectAsState()

    LaunchedEffect(origin, destination) {
        if (origin.isNotEmpty() && destination.isNotEmpty()) {
            Log.d("origin place_id", origin)
            Log.d("destination place_id", destination)
            routesViewModel.fetchRoute("place_id:$origin", "place_id:$destination", context)
        }
    }

    when {
        routeState?.isSuccess == true -> {
            val routeResponse = routeState?.getOrNull()
            val routes = routeResponse!!.routes

            for(route in routes){
                encodedPolylinePoints = route.overview_polyline.points
                val legs = route.legs
                for(leg in legs){
                    val steps = leg.steps
                    var transitName = ""
                    for(step in steps){
                        if(step.travel_mode.equals("TRANSIT")){
                            transitName = step.transit_details.headsign
                        }
                    }
                    Log.d("--->",transitName + "\n" + leg.end_address + "\n" + leg.arrival_time.text + "\n" + leg.duration.text)
                    busList.add(BusInfo(transitName, leg.end_address, leg.arrival_time.text, leg.duration.text))
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                // Bus schedule table takes up the top space and scrolls if needed
                BusScheduleTable(
                    busList = busList,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // RouteView also takes up a part of the available space
                TripView(
                    modifier = Modifier
                        .weight(1f) // Ensure the map takes up remaining space
                        .fillMaxWidth(),
                    originLatLng,
                    destinationLatLng,
                    encodedPolylinePoints
                )

                // Primary button aligned at the bottom
                PrimaryButton(
                    txt = "Show this Route",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            val jsonData = Gson().toJson(routeResponse)
                            val encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())
                            navController!!.navigate(TripRoutes.TripDetails.route.replace("{data}", encodedData))
                        }
                )
            }

        }
        routeState?.isFailure == true -> {
            val exception = routeState?.exceptionOrNull()
            Text(text = exception.toString())
        }
        else -> {
            Text(text = "LOADING")
        }
    }

}

fun getStatusColor(status: String): Color {
    // Extract the number of minutes from the string
    val minutes = status.takeWhile { it.isDigit() }.toIntOrNull()

    return when {
        minutes == null -> Color.LightGray // Handle unexpected formats
        minutes < 10 -> {
            // Color for hurry
            Color(0xFFFFC107) // Light orange (example for hurry)
        }
        minutes < 20 -> {
            // Color for below 20 minutes
            Color(0xFFFFF7CC) // Light yellow
        }
        else -> {
            // Color for 20 minutes or more
            Color(0xFFDFF6DD) // Light green or any other default color
        }
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(p)
    }
    return poly
}
