package com.sanapplications.cantransit.screens.trip_screen

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.api.resoponse_model.RouteResponse
import com.sanapplications.cantransit.api.resoponse_model.Step
import com.sanapplications.cantransit.graphs.RootGraph
import com.sanapplications.cantransit.graphs.TripRoutes
import com.sanapplications.cantransit.models.BusInfo
import com.sanapplications.cantransit.models.TripHistoryModel
import com.sanapplications.cantransit.repository.SettingsRepository
import com.sanapplications.cantransit.screens.settings_screen.SettingsViewModel
import com.sanapplications.cantransit.ui.theme.PrimaryColor
import com.sanapplications.cantransit.widgets.PrimaryButton
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    navController: NavHostController,
    routeResponse: RouteResponse,
    sharedPreferences: SharedPreferences
) {
    val context = LocalContext.current

    var routeSelected by remember { mutableStateOf(false) }

    val settingsRepository = SettingsRepository(sharedPreferences)
    val settingsViewModel = SettingsViewModel(settingsRepository)

    // Check if routes are available
    if (routeResponse.routes.isEmpty()) {
        Text("No routes available.", fontFamily = FontFamily(Font(R.font.inter_regular)), style = MaterialTheme.typography.bodyMedium)
        return
    }

    // Get the first route (or implement logic to select the desired route)
    val route = routeResponse.routes.first()

    // Extract the legs from the route (assuming one leg for simplicity)
    val leg = route.legs.first()

    // Scaffold to set up the app bar
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxHeight() // Make sure to fill the height
        ) {
            // Loop through the steps in the leg
            LazyColumn(
                modifier = Modifier.weight(1f) // Take up available space
            ) {
                items(leg.steps) { step ->
                    StepItem(step)
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space above the button

            // Primary button aligned at the bottom
            if(routeSelected){
                PrimaryButton(
                    txt = "Go To Home",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            navController.popBackStack()
                            navController.popBackStack()
                        }
                )
            }
            else {
                PrimaryButton(
                    txt = "Book Schedule Ride",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clickable {
                            Toast.makeText(context, "Route Added", Toast.LENGTH_SHORT).show()
                            routeSelected = true
                            var transitName = ""
                            var transitPrice = 0.0f
                            val routes = routeResponse.routes
                            for (route in routes) {
                                val legs = route.legs
                                for (leg in legs) {
                                    val steps = leg.steps
                                    for (step in steps) {
                                        if (step.travel_mode.equals("TRANSIT")) {
                                            transitName = step.transit_details.headsign
                                            transitPrice = 3.0f
                                        }
                                    }
                                }
                            }
                            settingsViewModel.addTripToHistory(
                                TripHistoryModel(
                                    transitName = transitName,
                                    fromLocation = routeResponse.routes[0].legs[0].start_address,
                                    toLocation = routeResponse.routes[0].legs[0].end_address,
                                    price = transitPrice
                                )
                            )
                        }
                )
            }

        }
    }

}

@Composable
fun StepItem(step: Step) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Step instructions with a larger font size
            Text(
                text = step.html_instructions.replace("+", " "),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily(Font(R.font.inter_semi_bold))),
                fontWeight = FontWeight.Bold // Optional, as the font is already semi-bold
            )

            // Row for distance and duration with icons
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Distance",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Distance: ${step.distance.text}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily(Font(R.font.inter_medium)))
                )
            }

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Duration",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Duration: ${step.duration.text}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily(Font(R.font.inter_medium)))
                )
            }
        }
    }
}
