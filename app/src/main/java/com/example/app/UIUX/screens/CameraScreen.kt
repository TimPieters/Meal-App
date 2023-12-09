package com.example.app.UIUX.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.AppContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.GradientBackground
import com.example.app.R
import com.example.app.TopBar
import com.example.app.fadingEdge

val topBottomFade = Brush.verticalGradient(0f to Color.Transparent, 0.2f to Color.Red, 0.8f to Color.Red, 1f to Color.Transparent)
@Preview
@Composable
fun CameraScreen() {
    GradientBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {TopBar(onBackClicked = {},
            profilePicturePainter = painterResource(id = R.drawable.topbarimage_placeholder), // Replace with an actual image resource
            onProfileClicked = {})
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.White, shape = RoundedCornerShape(50)) // Oval shape
                    .padding(horizontal = 20.dp, vertical = 10.dp) // Padding inside the box

            ) {
                Text(
                    "Take a picture of your fridge!",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.fridge_camera_screen),
            contentDescription = "Fridge picture",
            contentScale = ContentScale.FillWidth, // fill the width of the screen, might crop the image
            modifier = Modifier
                .fillMaxWidth()
                .fadingEdge(topBottomFade)
        )

    }
        AppContent()
}}