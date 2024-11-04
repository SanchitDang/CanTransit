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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import com.sanapplications.cantransit.factory.SettingsViewModelFactory
import com.sanapplications.cantransit.graphs.RootGraph
import com.sanapplications.cantransit.graphs.RootNavigationGraph
import com.sanapplications.cantransit.repository.SettingsRepository
import com.sanapplications.cantransit.screens.settings_screen.SettingsScreen
import com.sanapplications.cantransit.screens.settings_screen.SettingsViewModel
import com.sanapplications.cantransit.ui.theme.CanTransitTheme
import com.sanapplications.cantransit.ui.theme.PrimaryLightColor
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Places SDK
        val mapsApiKey = getString(R.string.MAPS_API_KEY)
        Places.initialize(applicationContext, mapsApiKey)

        // Initialize SharedPreferences and Repository
        val sharedPreferences = getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        val settingsRepository = SettingsRepository(sharedPreferences)

        // Set the content of the activity
        setContent {
            CanTransitTheme {
                // Provide the ViewModel using the factory
                val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))
                Splash(settingsViewModel, sharedPreferences)
            }
        }
    }

    @Composable
    private fun Splash(settingsViewModel: SettingsViewModel, sharedPreferences: SharedPreferences) {
        val isFirstLaunch by settingsViewModel.isFirstLaunch.collectAsState()
        val splashVisible = remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(2000L)
            splashVisible.value = false
        }

        if (isFirstLaunch) {
            settingsViewModel.setFirstLaunchComplete()
            RootNavigationGraph(navController = rememberNavController(), sharedPreferences = sharedPreferences, startDestination = RootGraph.AUTHENTICATION)
        } else {
            RootNavigationGraph(navController = rememberNavController(), sharedPreferences = sharedPreferences, startDestination = RootGraph.TRIP)
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
