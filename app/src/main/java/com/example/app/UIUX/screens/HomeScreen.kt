package com.example.app.UIUX.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.GradientBackground
import com.example.app.R
import com.example.app.StandardizedButton
import com.example.app.fadingEdge
import com.example.app.ui.theme.PlayfulFontFamily

val bottomFade = Brush.verticalGradient(0.9f to Color.Red, 1f to Color.Transparent)
@Composable
fun HomeScreen() {
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
                    .fadingEdge(bottomFade)
            )

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome to the App", color = Color.White,
                    fontFamily = PlayfulFontFamily, fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(32.dp))
                val onContinueClicked = {
                    // Handle the continue action
                }
                StandardizedButton(
                    text = "Continue to app",
                    onClick = onContinueClicked
                )
            }
        }
    }
}
