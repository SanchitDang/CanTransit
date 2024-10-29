package com.sanapplications.cantransit.screens.auth_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanapplications.cantransit.R
import com.sanapplications.cantransit.ui.theme.PrimaryColor

@Composable
fun StartScreen(
    onClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onForgotClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Full-screen background image
        Image(
            painter = painterResource(id = R.drawable.startbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Text at the top-left with multiple elements
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // "Welcome to" in black bold
            Text(
                text = "Welcome to",
                fontSize = 36.sp,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                color = Color.Black
            )

            // "CanTransit" in blue bold, larger font
            Text(
                text = "CanTransit",
                fontSize = 60.sp,
                fontFamily = FontFamily(Font(R.font.inter_bold)),
                color = PrimaryColor
            )

            // "The best way to explore your city" in small grey bold text
            Text(
                text = "The best way to explore your city",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                color = Color.Gray
            )

            // Additional relatable tagline
            Text(
                text = "Find the best routes and transit options",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                color = Color.Gray
            )
        }

        // Button at the bottom-left
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor
                )
            ) {
                Text(text = "Continue", fontSize = 22.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStartScreen(){
    StartScreen({},{},{},{})
}
