package com.sanapplications.cantransit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sanapplications.cantransit.ui.theme.CanTransitTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanTransitTheme {
                SplashScreenUI()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun SplashScreenUI() {
        LaunchedEffect(key1 = true) {
            delay(SPLASH_DURATION)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)  // Transition Animation
            finish()  // Finish SplashActivity
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF50ACF7)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Screen Logo"
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Prevent back press during splash screen
    }

    companion object {
        private const val SPLASH_DURATION = 2000L // Configurable delay time
    }
}
