package com.sanapplications.cantransit.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.sanapplications.cantransit.ui.theme.PrimaryColor

@Composable
fun HomeScreen(navController: NavHostController?) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(10.dp))
        HomeScreenHeader()
        Spacer(Modifier.height(12.dp))
        HomeCard()
        Spacer(Modifier.height(18.dp))
        HomeLastTrips()
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

                // Text is now centered
                Text(
                    text = "Hey, Sanchit",
                    fontFamily = FontFamily(
                        Font(R.font.inter_semi_bold)
                    ),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

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
fun HomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth().height(130.dp).padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryColor)
    ){
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                Text(text = "CanadaRide", fontSize = 19.sp, color = Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "", tint = Color.White)
            }
            Row {
                Column {
                    Text(text = "Balance", fontSize = 13.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_regular)))
                    Text(text = "$120", fontSize = 16.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(text = "Pass id", fontSize = 13.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_regular)))
                    Text(text = "890 672", fontSize = 16.sp, color= Color.White, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))
                }
            }
        }
    }
}

@Composable
fun HomeLastTrips() {
    // Sample data
    val items = List(10) { "Item $it" } // Replace with your data

    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(text = "Your last trips", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.inter_medium)))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
    }

    Spacer(Modifier.height(4.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Set number of columns to 2
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp) // Optional: Padding around the grid
    ) {
        items(items.size) { index ->
            Card(
                modifier = Modifier
                    .padding(8.dp) // Padding between cards
                    .aspectRatio(1f), // Maintain a square aspect ratio
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bus),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = items[index],
                            fontFamily = FontFamily(
                                Font(R.font.inter_semi_bold)
                            ), fontSize = 16.sp,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "From",
                            fontFamily = FontFamily(
                                Font(R.font.inter_regular)
                            ), fontSize = 13.sp,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "To",
                            fontFamily = FontFamily(
                                Font(R.font.inter_regular)
                            ), fontSize = 13.sp,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.weight(1f)) {
                        Text(text = "Price:", fontSize = 13.sp, fontFamily = FontFamily(Font(R.font.inter_medium)))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "$5.0", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.inter_semi_bold)))

                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(null)
}
