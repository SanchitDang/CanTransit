package com.sanapplications.cantransit.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanapplications.cantransit.R

@Composable
fun TripView2(startPoint: LatLng, endPoint: LatLng) {
    val context = LocalContext.current

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

    // Use LaunchedEffect to move the camera when startPoint or endPoint changes
    LaunchedEffect(startPoint, endPoint) {
        // Update marker positions
        startMarkerState.position = startPoint
        endMarkerState.position = endPoint

        // Move camera to include both points
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    // Initialize Maps SDK and load custom icons once
    var startIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var endIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(Unit) {
        // Initialize Google Maps SDK
        MapsInitializer.initialize(context)
        // Load custom marker icons after initialization
        startIcon = BitmapDescriptorFactory.fromResource(R.drawable.start_marker)
        endIcon = BitmapDescriptorFactory.fromResource(R.drawable.end_marker)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
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
            }
        }
    }
}
