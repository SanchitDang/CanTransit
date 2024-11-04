package com.sanapplications.cantransit.screens.home_screen

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.repository.SettingsRepository
import com.sanapplications.cantransit.screens.settings_screen.SettingsViewModel
import com.sanapplications.cantransit.ui.theme.PrimaryColor
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavHostController, sharedPreferences: SharedPreferences) {

    val repository = remember { SettingsRepository(sharedPreferences) }
    val settingsViewModel = remember { SettingsViewModel(repository) }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(10.dp))
        HomeScreenHeader()
        Spacer(Modifier.height(12.dp))
        HomeCard( settingsViewModel )
        Spacer(Modifier.height(18.dp))
        HomeLastTrips( settingsViewModel )
    }
}

@Composable
fun HomeScreenHeader() {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(40.dp)
                )

                // Spacer before the Text to push it to the center
                Spacer(modifier = Modifier.weight(1f))

                // Greeting Tex
                GreetingText()

                // Spacer after the Text to push the icon to the right
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    }
}

@Composable
fun HomeCard(settingsViewModel: SettingsViewModel) {

    val cardSettings by settingsViewModel.cardSettings.collectAsState()

    val cardName by remember { mutableStateOf(cardSettings.cardName) }
    val cardNumber by remember { mutableStateOf(cardSettings.cardNumber) }
    val cardBalance by remember { mutableStateOf(cardSettings.cardBalance.toString()) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor)
    ){
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                Text(text = cardName, fontSize = 19.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "", tint = Color.White)
            }
            Row {
                Column {
                    Text(text = "Balance", fontSize = 13.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_regular)))
                    Text(text = "$$cardBalance", fontSize = 16.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(text = "Pass id", fontSize = 13.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_regular)))
                    Text(text = cardNumber, fontSize = 16.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                }
            }
        }
    }
}

@Composable
fun HomeLastTrips(settingsViewModel: SettingsViewModel) {
    // Collect the tripsHistory list from ViewModel as state
    val trips = settingsViewModel.tripsHistory.collectAsState().value

    // Heading
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(text = "Your last trips", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.inter_medium)))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
    }

    Spacer(Modifier.height(4.dp))

    if (trips.isEmpty()) {
        // Show message when the trip history is empty
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No travel history.",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Display trips in a grid when history is not empty
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(trips.size) { index ->
                val trip = trips[index] // Current trip data

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.bus),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = trip.transitName.replace("+", " "),
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis

                            )
                        }
                        Spacer(Modifier.height(10.dp))

                        // Display "From" location
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "From: ${trip.fromLocation.replace("+", " ")}",
                                fontFamily = FontFamily(Font(R.font.inter_regular)),
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Display "To" location
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "To: ${trip.toLocation.replace("+", " ")}",
                                fontFamily = FontFamily(Font(R.font.inter_regular)),
                                fontSize = 13.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.height(10.dp))

                        // Display trip price
                        Row(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Price:",
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium))
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "$${trip.price}",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingText() {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = if (currentHour < 12) "Good Morning" else "Good Evening"

    Text(
        text = greeting,
        fontFamily = FontFamily(
            Font(R.font.inter_semi_bold)
        ),
        fontSize = 18.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
