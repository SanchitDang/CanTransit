package com.sanapplications.cantransit.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.sanapplications.cantransit.R


@Composable
fun SampleMapsView() {
    val canada = LatLng(43.59428991196505, -79.64704467174485)
    val canadaMarkerState = rememberMarkerState(position = canada)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(canada, 15f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)
            ) {
                Marker(
                    state = canadaMarkerState,
                    title = "Canada",
                    snippet = "Square One"
                )
            }

            Image(
                painter = painterResource(R.drawable.location_button),
                contentDescription = "Go to Current Location",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(14.dp)
                    .size(40.dp)
                    .clickable {
                        val currentLocation = LatLng(43.59626955323733, -79.63955026886522)
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(currentLocation, 17f)
                        )
                    },
            )
        }
    }
}