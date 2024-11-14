package com.example.app.UIUX.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.app.R
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay

@Composable
fun MealLoadingScreen() {
    val messages = listOf(
        "Checking out ingredients...",
        "Taking into account preferences...",
        "Taking into account cuisine...",
        "Taking into account difficulty...",
        "Taking into account serving size...",
        "Taking into account nutritional profile...",
        "I now have a good idea!",
        "Generating recipes...",
        "Done!"
    )

    var currentMessageIndex by remember { mutableStateOf(0) }

    // Automatically cycle through messages with fading effect
    LaunchedEffect(Unit) {
        while (currentMessageIndex < messages.size) {
            delay(2500)  // Display each message for 2 seconds
            if (currentMessageIndex < messages.size - 1) {
                currentMessageIndex++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lottie Animation
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.generate_meals_animation)) // Replace with actual animation file
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Animated text with fading effect
        AnimatedMessage(
            text = messages[currentMessageIndex],
            fontSize = 22,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MealLoadingScreenPreview() {
    MealLoadingScreen()
}
