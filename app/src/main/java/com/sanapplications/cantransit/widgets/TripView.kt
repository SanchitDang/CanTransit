package com.sanapplications.cantransit.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.screens.trip_screen.decodePolyline
import com.sanapplications.cantransit.ui.theme.PrimaryColor

@Composable
fun TripView(
    modifier: Modifier = Modifier,
    originLatLng: String,
    destinationLatLng: String,
    encodedPolylinePoints: String
) {
    val context = LocalContext.current

    // Define the two points (start and end)
    val startPoint = LatLng(originLatLng.split(",").first().toDouble(), originLatLng.split(",").last().toDouble())
    val endPoint = LatLng(destinationLatLng.split(",").first().toDouble(), destinationLatLng.split(",").last().toDouble())

    // Marker states for the two points
    val startMarkerState = rememberMarkerState(position = startPoint)
    val endMarkerState = rememberMarkerState(position = endPoint)

    // Initialize BitmapDescriptors for start and end icons
    var startIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var endIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    // Initialize Google Maps and load icons
    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context)
        startIcon = BitmapDescriptorFactory.fromResource(R.drawable.start_marker)
        endIcon = BitmapDescriptorFactory.fromResource(R.drawable.end_marker)
    }

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

    // Decode the polyline string
    val polylinePoints = remember { decodePolyline(encodedPolylinePoints) }
    println("Polyline points in MapScreen: $polylinePoints")

    Column(modifier = modifier.height(400.dp)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true
            )
        ) {
            // Marker for start point with a custom icon (if loaded)
            if (startIcon != null) {
                Marker(
                    state = startMarkerState,
                    title = "Start Point",
                    snippet = "This is the start location",
                    icon = startIcon
                )
            }

            // Marker for end point with a different custom icon (if loaded)
            if (endIcon != null) {
                Marker(
                    state = endMarkerState,
                    title = "End Point",
                    snippet = "This is the end location",
                    icon = endIcon
                )
            }

            // Draw the polyline with updated attributes
            Polyline(
                points = polylinePoints,
                color = PrimaryColor,
            )
        }
    }
}