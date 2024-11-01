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

@Composable
fun TripScreen(navController: NavHostController?) {
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
            RouteView(
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

@Composable
fun LocationPickView(tripViewModel: TripViewModel) {
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
                                                        pickLatLng = fetchPlaceLatLng(
                                                            suggestion.placeId,
                                                            placesClient
                                                        )
                                                        tripViewModel.updateStartLocationPlaceId(suggestion.placeId)
                                                        tripViewModel.updateStartLocation(LatLng(
                                                            pickLatLng!!.first, pickLatLng!!.second))
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
                                                        dropLatLng = fetchPlaceLatLng(
                                                            suggestion.placeId,
                                                            placesClient
                                                        )
                                                        tripViewModel.updateEndLocationPlaceId(suggestion.placeId)
                                                        tripViewModel.updateEndLocation(LatLng(
                                                            dropLatLng!!.first, dropLatLng!!.second))
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
fun RouteView(startPoint: LatLng, endPoint: LatLng) {
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
fun TransitSelectionView(navController: NavHostController, tripViewModel: TripViewModel) {
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
                            val startLatLngString: String = tripViewModel.startLocationLatLng.value.latitude.toString()+","+tripViewModel.startLocationLatLng.value.longitude.toString()
                            val endLatLngString: String = tripViewModel.endLocationLatLng.value.latitude.toString()+","+tripViewModel.endLocationLatLng.value.longitude.toString()
                            selectedTransport = "Bus"
                            navController.navigate("location_transit/${tripViewModel.startLocationPlaceId.value}/${tripViewModel.endLocationPlaceId.value}/${startLatLngString}/${endLatLngString}")

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
