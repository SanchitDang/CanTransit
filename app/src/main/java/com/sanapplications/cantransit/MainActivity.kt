package com.sanapplications.cantransit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import com.sanapplications.cantransit.graphs.RootNavigationGraph
import com.sanapplications.cantransit.screens.settings_screen.SettingsScreen
import com.sanapplications.cantransit.ui.theme.CanTransitTheme
import com.sanapplications.cantransit.ui.theme.PrimaryLightColor
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Places SDK
        val mapsApiKey = getString(R.string.MAPS_API_KEY)
        Places.initialize(applicationContext, mapsApiKey)
        val sharedPreferences = getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        setContent {
            CanTransitTheme {
                Splash(sharedPreferences)  // Show splash and then main screen
            }
        }
    }

    @Composable
    private fun Splash(sharedPreferences: SharedPreferences) {
        // State to control the visibility of the splash screen
        val splashVisible = remember { mutableStateOf(true) }

        // Launch the splash screen effect
        LaunchedEffect(Unit) {
            delay(2000L)
            splashVisible.value = false  // Hide the splash screen
        }

        if (splashVisible.value) {
            SplashScreenUI()  // Show the splash screen
        } else {
            RootNavigationGraph(navController = rememberNavController(), sharedPreferences = sharedPreferences)  // Show the main screen after splash
        }
    }

    @Composable
    private fun SplashScreenUI() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryLightColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Screen Logo"
            )
        }
    }

}

