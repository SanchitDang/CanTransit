package com.sanapplications.cantransit.screens.trip_screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.graphs.TripRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.maps.android.compose.rememberMarkerState as rememberMarkerState1

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

@Composable
fun TripScreen(navController: NavHostController?) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column {
            LocationPickView()
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            MapsView()
        }
        Column {
            if (navController != null) {
                TransitSelectionView(navController)
            }
        }

    }
}

@Composable
fun LocationPickView() {
    val placesClient = Places.createClient(LocalContext.current)
    val sessionToken = remember { AutocompleteSessionToken.newInstance() }

    var pickLocation by remember { mutableStateOf("") }
    var dropLocation by remember { mutableStateOf("") }
    var pickSuggestions by remember { mutableStateOf(listOf<PlaceSuggestion>()) }
    var dropSuggestions by remember { mutableStateOf(listOf<PlaceSuggestion>()) }
    var pickLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var dropLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.background(color = Color.White)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box {
                Column {
                    // "Pick Location" Autocomplete
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
                    ) {
                        Column {
                            TextField(
                                value = pickLocation,
                                onValueChange = {
                                    pickLocation = it
                                    coroutineScope.launch(Dispatchers.IO) {
                                        pickSuggestions = getAutocompleteSuggestions(it, placesClient, sessionToken)
                                    }
                                },
                                label = { Text("Pick Location") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFFBFBFB),
                                    unfocusedContainerColor = Color(0xFFFBFBFB),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.Black
                                )
                            )

                            if (pickSuggestions.isNotEmpty()) {
                                LazyColumn {
                                    items(pickSuggestions) { suggestion ->
                                        Text(
                                            text = suggestion.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    pickLocation = suggestion.name
                                                    coroutineScope.launch {
                                                        pickLatLng = fetchPlaceLatLng(suggestion.placeId, placesClient)
                                                    }
                                                    pickSuggestions = emptyList()
                                                }
                                                .padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // "Drop Location" Autocomplete
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
                    ) {
                        Column {
                            TextField(
                                value = dropLocation,
                                onValueChange = {
                                    dropLocation = it
                                    coroutineScope.launch(Dispatchers.IO) {
                                        dropSuggestions = getAutocompleteSuggestions(it, placesClient, sessionToken)
                                    }
                                },
                                label = { Text("Drop Location") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFFBFBFB),
                                    unfocusedContainerColor = Color(0xFFFBFBFB),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.Black
                                )
                            )

                            if (dropSuggestions.isNotEmpty()) {
                                LazyColumn {
                                    items(dropSuggestions) { suggestion ->
                                        Text(
                                            text = suggestion.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    dropLocation = suggestion.name
                                                    coroutineScope.launch {
                                                        dropLatLng = fetchPlaceLatLng(suggestion.placeId, placesClient)
                                                    }
                                                    dropSuggestions = emptyList()
                                                }
                                                .padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(R.drawable.up_down_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.CenterEnd)
                        .absoluteOffset(x = (-20).dp)
                )
            }
        }
    }

    // Display selected coordinates if available
//    pickLatLng?.let { Text("Pick Location LatLng: $it") }
//    dropLatLng?.let { Text("Drop Location LatLng: $it") }
}

// Data class for holding suggestion data
data class PlaceSuggestion(val name: String, val placeId: String)

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

@Composable
fun MapsView() {

    val canada = LatLng(43.59428991196505, -79.64704467174485)
    val canadaMarkerState = rememberMarkerState1(position = canada)
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

@Composable
fun TransitSelectionView(navController: NavHostController) {
    // State to track the selected transport mode
    var selectedTransport by remember { mutableStateOf("") }

    Column(modifier = Modifier.background(color = Color.White)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Bus" Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTransport = "Bus"
                            navController.navigate(TripRoutes.AvailableTransitRoutes.route)
                            }, // Click to select "Bus"
                    colors = if (selectedTransport == "Bus") CardDefaults.cardColors(Color(0xFFE8F1FD)) else CardDefaults.cardColors(
                        Color.White),

                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bus_grey),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Bus",
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // "Metro" Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTransport = "Metro" }, // Click to select "Metro"
                    colors = if (selectedTransport == "Metro") CardDefaults.cardColors(Color(0xFFE8F1FD)) else CardDefaults.cardColors(
                        Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.metro_grey),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Metro",
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // "Tram" Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp)
                        .clickable { selectedTransport = "Tram" }, // Click to select "Tram"
                    colors = if (selectedTransport == "Tram") CardDefaults.cardColors(Color(0xFFE8F1FD)) else CardDefaults.cardColors(
                        Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tram_grey),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Tram",
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    TripScreen(null)
}