package com.sanapplications.cantransit.screens.settings_screen

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.models.CardSettingsModel
import com.sanapplications.cantransit.repository.SettingsRepository
import com.sanapplications.cantransit.ui.theme.PrimaryColor
import com.sanapplications.cantransit.ui.theme.PrimaryLightColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController?,  sharedPreferences: SharedPreferences) {
    // Initialize the repository and ViewModel manually
    val repository = remember { SettingsRepository(sharedPreferences) }
    val viewModel = remember { SettingsViewModel(repository) }

    // Collect state from the ViewModel
    val cardSettings by viewModel.cardSettings.collectAsState()

    var cardName by remember { mutableStateOf(cardSettings.cardName) }
    var cardNumber by remember { mutableStateOf(cardSettings.cardNumber) }
    var cardBalance by remember { mutableStateOf(cardSettings.cardBalance.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily(
                                Font(R.font.inter_semi_bold)
                            ),
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Card Information",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(
                        Font(R.font.inter_semi_bold)
                    ),
                    color = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = { Text("Card Name") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = cardBalance,
                onValueChange = { cardBalance = it },
                label = { Text("Card Balance") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedSettings = CardSettingsModel(
                        cardName = cardName,
                        cardNumber = cardNumber,
                        cardBalance = cardBalance.toFloatOrNull() ?: 0.0f
                    )
                    viewModel.updateCardSettings(updatedSettings)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Save Settings",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily(
                            Font(R.font.inter_semi_bold)
                        )
                    )
                )
            }
        }
    }
}
