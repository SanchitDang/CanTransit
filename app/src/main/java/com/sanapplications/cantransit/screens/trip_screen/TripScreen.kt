package com.sanapplications.cantransit.screens.trip_screen

import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.sanapplications.cantransit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.maps.android.compose.rememberMarkerState as rememberMarkerState1

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.maps.android.compose.rememberMarkerState
import com.sanapplications.cantransit.graphs.TripRoutes
import com.sanapplications.cantransit.models.PlaceSuggestion
import com.sanapplications.cantransit.widgets.LocationPickView
import com.sanapplications.cantransit.widgets.TransitSelectionView
import com.sanapplications.cantransit.widgets.TripView2

@Composable
fun TripScreen(navController: NavHostController?, sharedPreferences: SharedPreferences) {
    // Get an instance of TripViewModel
    val tripViewModel: TripViewModel = viewModel()

    // Collect the state for start and end location
    val startLocationLatLng by tripViewModel.startLocationLatLng.collectAsState()
    val endLocationLatLng by tripViewModel.endLocationLatLng.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Column {
            LocationPickView(tripViewModel)
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Use the collected LatLng values from the ViewModel
            TripView2(
                startLocationLatLng,
                endLocationLatLng
            )
        }
        Column {
            if (navController != null) {
                TransitSelectionView(navController, tripViewModel)
            }
        }
    }
}

// Helper function to get autocomplete suggestions
suspend fun getAutocompleteSuggestions(
    query: String,
    placesClient: PlacesClient,
    sessionToken: AutocompleteSessionToken
): List<PlaceSuggestion> {
    if (query.isEmpty()) return emptyList()

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setSessionToken(sessionToken)
        .build()

    return try {
        val response = placesClient.findAutocompletePredictions(request).await()
        response.autocompletePredictions.map { PlaceSuggestion(it.getPrimaryText(null).toString(), it.placeId) }
    } catch (e: Exception) {
        emptyList()
    }
}

// Function to fetch place latitude and longitude using placeId
suspend fun fetchPlaceLatLng(placeId: String, placesClient: PlacesClient): Pair<Double, Double>? {
    val placeFields = listOf(Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.builder(placeId, placeFields).build()

    return try {
        val response = placesClient.fetchPlace(request).await()
        val latLng = response.place.latLng
        latLng?.let { it.latitude to it.longitude }
    } catch (e: Exception) {
        null
    }
}
