package com.sanapplications.cantransit.widgets

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.graphs.TripRoutes
import com.sanapplications.cantransit.screens.trip_screen.TripViewModel

@Composable
fun TransitSelectionView(navController: NavHostController, tripViewModel: TripViewModel) {
    val context = LocalContext.current

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
                            navController.navigate(
                                TripRoutes.AvailableTrips.route
                                .replace("{origin}", tripViewModel.startLocationPlaceId.value)
                                .replace("{destination}", tripViewModel.endLocationPlaceId.value)
                                .replace("{originLatLng}", startLatLngString)
                                .replace("{destinationLatLng}", endLatLngString)
                            )
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
                        .clickable {
                            selectedTransport = "Metro"
                            Toast.makeText(context, "This feature would be added soon", Toast.LENGTH_SHORT).show()
                        },
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
                        .clickable {
                            selectedTransport = "Tram"
                            Toast.makeText(context, "This feature would be added soon", Toast.LENGTH_SHORT).show()
                        },
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
