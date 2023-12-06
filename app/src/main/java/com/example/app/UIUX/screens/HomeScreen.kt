package com.example.app.UIUX.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app.GradientBackground
import com.example.app.R
import com.example.app.ui.theme.PlayfulFontFamily

@Composable
fun HomeScreen(onContinueClicked: () -> Unit) {
    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background image with gradient overlay
            Image(
                painter = painterResource(id = R.drawable.background_image),
                contentDescription = "Background",
                contentScale = ContentScale.FillWidth, // fill the width of the screen, might crop the image
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 260.dp) // Minimum height for the image, adjust as needed
                    .align(Alignment.TopStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF394b4a)),
                            startY = Float.POSITIVE_INFINITY, // Start fading at the bottom of the image
                            endY = 0f
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome to the App", color = Color.White,fontFamily = PlayfulFontFamily)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onContinueClicked,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9eb1b8))
                ) {
                    Text("Continue to app",color = Color.White,
                        fontFamily = PlayfulFontFamily)
                }
            }
        }
    }
}
