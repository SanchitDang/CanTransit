package com.sanapplications.cantransit.screens.available_routes_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanapplications.cantransit.screens.trip_screen.TripViewModel
import com.sanapplications.cantransit.ui.theme.PrimaryColor

@Composable
fun AvailableRoutesScreen(
    navController: NavHostController?,
    origin: String,
    destination: String,
    originLatLng: String,
    destinationLatLng: String
) {

    val busList = remember { mutableStateListOf<BusInfo>() }

    val context = LocalContext.current
    val routesViewModel: RoutesViewModel = viewModel()
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
                val legs = route.legs
                for(leg in legs){
                    val steps = leg.steps
                    var transitName = ""
                    for(step in steps){
                        if(step.travel_mode.equals("TRANSIT")){
                            transitName = step.html_instructions
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
                        .weight(1f) // Assign weight to make it occupy available space
                        .fillMaxWidth()
                )

                // RouteView also takes up a part of the available space
                RouteView(
                    modifier = Modifier
                        .weight(1f) // Ensure the map takes up remaining space
                        .fillMaxWidth(),
                    originLatLng,
                    destinationLatLng
                )

                // Primary button aligned at the bottom
                PrimaryButton(
                    txt = "Show this Route",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
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

@Composable
fun BusScheduleTable(busList: List<BusInfo>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        // Heading row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bus", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Destination", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "ETA", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Status", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(busList.size) { index ->
                BusScheduleRow(busInfo = busList[index])
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RouteView(modifier: Modifier = Modifier, originLatLng: String, destinationLatLng: String) {
    // Define the two points (start and end)
    val startPoint = LatLng(originLatLng.split(",").first().toDouble(), originLatLng.split(",").last().toDouble())
    val endPoint = LatLng(destinationLatLng.split(",").first().toDouble(), destinationLatLng.split(",").last().toDouble())

    // Marker states for the two points
    val startMarkerState = rememberMarkerState(position = startPoint)
    val endMarkerState = rememberMarkerState(position = endPoint)

    // Create LatLngBounds to include both start and end points
    val bounds = LatLngBounds.builder()
        .include(startPoint)
        .include(endPoint)
        .build()

    // Camera position state that automatically adjusts to include both markers
    val cameraPositionState = rememberCameraPositionState()

    // Use LaunchedEffect to move the camera when the view is first composed
    LaunchedEffect(Unit) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    Column(modifier = modifier.height(400.dp)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true
            )
        ) {
            // Marker for start point with a custom icon
            Marker(
                state = startMarkerState,
                title = "Start Point",
                snippet = "This is the start location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) // Green icon
            )

            // Marker for end point with a different custom icon
            Marker(
                state = endMarkerState,
                title = "End Point",
                snippet = "This is the end location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) // Red icon
            )
        }
    }
}

// Data class for Bus info
data class BusInfo(
    val busNumber: String,
    val destination: String,
    val eta: String,
    val status: String
)

// Colors based on status
fun getStatusColor(status: String): Color {
    return when (status) {
        "On time" -> Color(0xFFDFF6DD) // Light green
        "Delayed" -> Color(0xFFFFF7CC) // Light yellow
        "Canceled" -> Color(0xFFFFE1E0) // Light red
        else -> Color.LightGray
    }
}

@Composable
fun BusScheduleRow(busInfo: BusInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = busInfo.busNumber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = busInfo.destination, fontSize = 16.sp)
        Text(text = busInfo.eta, fontSize = 16.sp)

        // Status box with background color
        Box(
            modifier = Modifier
                .background(getStatusColor(busInfo.status), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = busInfo.status, color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun PrimaryButton(txt: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(50.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = txt, textAlign = TextAlign.Center, color = Color.White)
        }
    }
}
