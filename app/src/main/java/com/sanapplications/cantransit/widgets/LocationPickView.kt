package com.sanapplications.cantransit.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.models.PlaceSuggestion
import com.sanapplications.cantransit.screens.trip_screen.TripViewModel
import com.sanapplications.cantransit.screens.trip_screen.fetchPlaceLatLng
import com.sanapplications.cantransit.screens.trip_screen.getAutocompleteSuggestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                                                        tripViewModel.updateStartLocation(
                                                            LatLng(
                                                            pickLatLng!!.first, pickLatLng!!.second)
                                                        )
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
                                                        tripViewModel.updateEndLocation(
                                                            LatLng(
                                                            dropLatLng!!.first, dropLatLng!!.second)
                                                        )
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
