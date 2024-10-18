package com.sanapplications.cantransit

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
import com.sanapplications.cantransit.graphs.RootNavigationGraph
import com.sanapplications.cantransit.ui.theme.CanTransitTheme
import com.sanapplications.cantransit.ui.theme.PrimaryLightColor
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanTransitTheme {
                Splash()  // Show splash and then main screen
            }
        }
    }

    @Composable
    private fun Splash() {
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
            RootNavigationGraph(navController = rememberNavController())  // Show the main screen after splash
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

