package com.sanapplications.cantransit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sanapplications.cantransit.R

@Composable
fun LocationScreen(navController: NavHostController?) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocationPickView()
        MapsView()
        TransitSelectionView()
    }
}

@Composable
fun LocationPickView() {
    Column(modifier = Modifier.background(color = Color.White)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box {
                Column {
                    // First Card for "Pick Location"
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
                    ) {
                        TextField(
                            value = "", // You can bind this to a state to manage the input
                            onValueChange = { /* Handle pick location input */ },
                            label = { Text("Pick Location") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFFBFBFB),
                                unfocusedContainerColor = Color(0xFFFBFBFB),
                                focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                                unfocusedIndicatorColor = Color.Transparent, // Remove underline when unfocused
                                cursorColor = Color.Black // Customize the cursor color
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second Card for "Drop Location"
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFBFB))
                    ) {
                        TextField(
                            value = "", // You can bind this to a state to manage the input
                            onValueChange = { /* Handle drop location input */ },
                            label = { Text("Drop Location") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFFBFBFB),
                                unfocusedContainerColor = Color(0xFFFBFBFB),
                                focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                                unfocusedIndicatorColor = Color.Transparent, // Remove underline when unfocused
                                cursorColor = Color.Black // Customize the cursor color
                            )
                        )
                    }
                }

                // Arrow Image between the two Cards
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

@Composable
fun MapsView() {

}

@Composable
fun TransitSelectionView() {
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
                        .clickable { selectedTransport = "Bus" }, // Click to select "Bus"
                    colors = if (selectedTransport == "Bus") CardDefaults.cardColors(Color(0xFF1577EA)) else CardDefaults.cardColors(
                        Color.White),

                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // "Metro" Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTransport = "Metro" }, // Click to select "Metro"
                    colors = if (selectedTransport == "Metro") CardDefaults.cardColors(Color(0xFF1577EA)) else CardDefaults.cardColors(
                        Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                            fontSize = 16.sp
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
                    colors = if (selectedTransport == "Tram") CardDefaults.cardColors(Color(0xFF1577EA)) else CardDefaults.cardColors(
                        Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                            fontSize = 16.sp
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
    LocationScreen(null)
}