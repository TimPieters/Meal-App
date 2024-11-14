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
fun IngredientLoadingScreen() {
    val messages = listOf(
        "Looking at picture...",
        "Scanning fridge...",
        "Detecting ingredients...",
        "Preparing ingredient list...",
        "Done!"
    )

    var currentMessageIndex by remember { mutableStateOf(0) }

    // Automatically cycle through messages with fading effect
    LaunchedEffect(Unit) {
        while (currentMessageIndex < messages.size) {
            delay(2000)  // Display each message for 2 seconds
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

@Composable
fun AnimatedMessage(
    text: String,
    fontSize: Int,
    fontWeight: FontWeight,
    color: androidx.compose.ui.graphics.Color
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(text) {
        visible = false
        delay(100)  // Brief delay to prepare for the next message
        visible = true
    }

    // Fine-tuned animation with slide-up and fade-out effect
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = {it/2},  // Start slightly lower for a smoother slide-in
            animationSpec = tween(durationMillis = 700, easing = LinearEasing)
        ) + fadeIn(animationSpec = tween(durationMillis = 700, easing = LinearEasing)),
        exit = slideOutVertically(
            targetOffsetY = {-it/2},  // Slide up slightly less for a consistent flow
            animationSpec = tween(durationMillis = 700, easing = LinearEasing)
        )
        + fadeOut(animationSpec = tween(durationMillis = 700, easing = LinearEasing))
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun IngredientLoadingScreenPreview() {
    IngredientLoadingScreen()
}
