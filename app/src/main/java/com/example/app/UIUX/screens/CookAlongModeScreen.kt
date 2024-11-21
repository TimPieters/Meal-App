package com.example.app.UIUX.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.app.Instruction
import com.example.app.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CookAlongScreen(instructions: List<Instruction>, onBack: () -> Unit) {
    val pagerState = rememberPagerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cook Along Mode") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Pager
            HorizontalPager(
                count = instructions.size,
                state = pagerState,
                modifier = Modifier.weight(1f) // Take up most of the space
            ) { page ->
                val instruction = instructions[page]
                CookAlongStep(
                    instruction = instruction,
                    pageNumber = page + 1,
                    totalSteps = instructions.size
                )
            }

            // Dots Indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp) // Add spacing from the bottom
            ) {
                repeat(instructions.size) { index ->
                    IndicatorSingleDot(isSelected = pagerState.currentPage == index)
                }
            }
        }
    }
}


@Composable
fun CookAlongStep(instruction: Instruction, pageNumber: Int, totalSteps: Int) {
    val totalTime = extractTimeInSeconds(instruction.step) // Extract total time from the instruction
    val timerState = remember { mutableStateOf(totalTime) }
    val isRunning = remember { mutableStateOf(false) }

    LaunchedEffect(isRunning.value) {
        if (isRunning.value) {
            while (timerState.value > 0 && isRunning.value) {
                delay(1000L) // Decrement every second
                timerState.value -= 1
            }
        }
    }

    val animation = getAnimationForStep(instruction.step) // Fetch animation based on step content

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie Animation
        animation?.let {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(it))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(200.dp)
            )
        }

        // Step Text
        Text(
            text = instruction.step,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Timer UI
        if (totalTime > 0) {
            TimerUI(
                totalTime = totalTime,
                timerState = timerState,
                isRunning = isRunning
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Counter
        Text(
            text = "Step $pageNumber of $totalSteps",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}



@Composable
fun TimerUI(totalTime: Long, timerState: MutableState<Long>, isRunning: MutableState<Boolean>) {
    val progress = timerState.value.toFloat() / totalTime // Progress (0 to 1)

    // Countdown Indicator with Time
    CountDownIndicator(
        progress = progress,
        time = formatTime(timerState.value), // Pass formatted time
        size = 180, // Circle size
        stroke = 12 // Circle stroke width
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Play/Stop Button
    CountDownButton(
        isPlaying = isRunning.value,
        optionSelected = {
            isRunning.value = !isRunning.value // Toggle play/pause
            if (!isRunning.value) {
                // Reset timer if stopped
                timerState.value = totalTime
            }
        }
    )
}



@Composable
fun CountDownButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    optionSelected: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 90.dp)
    ) {

        Button(
            onClick = {
                optionSelected()
            },
            modifier =
            Modifier
                .height(70.dp)
                .width(200.dp),

            shape = RoundedCornerShape(25.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_200),
                contentColor = colorResource(id = R.color.white),
            ),

            ) {
            val pair = if (!isPlaying) {
                "START"
            } else {
                "STOP"
            }

            Text(
                pair,
                fontSize = 20.sp,
                color = Color.White,
            )
        }
    }
}

@Composable
fun CountDownIndicator(
    progress: Float,
    time: String,
    size: Int,
    stroke: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    // Ensure the progress circle and time are displayed properly
    Box(
        contentAlignment = Alignment.Center, // Ensures time is centered
        modifier = Modifier
            .size(size.dp) // Circular timer size
    ) {
        // Background Circle
        CircularProgressIndicatorBackGround(
            modifier = Modifier.fillMaxSize(),
            color = Color.LightGray, // Background circle color
            stroke = stroke
        )

        // Foreground Progress Circle
        CircularProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFF7043), // Foreground progress color
            strokeWidth = stroke.dp
        )

        // Time Display (Centered Text)
        Text(
            text = time,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black // Text color
        )
    }
}

@Composable
fun CircularProgressIndicatorBackGround(
    modifier: Modifier = Modifier,
    color: Color,
    stroke: Int
) {
    val style = with(LocalDensity.current) { Stroke(stroke.dp.toPx()) }

    Canvas(
        modifier = modifier,
        onDraw = {

            val innerRadius = (size.minDimension - style.width) / 2

            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 360f,
                topLeft = Offset(
                    (size / 2.0f).width - innerRadius,
                    (size / 2.0f).height - innerRadius
                ),
                size = Size(innerRadius * 2, innerRadius * 2),
                useCenter = false,
                style = style
            )
        }
    )
}


fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}


fun getAnimationForStep(step: String): Int? {
    return when {
        step.contains("cut", ignoreCase = true) -> R.raw.cutting_animation
        step.contains("oven", ignoreCase = true) -> R.raw.generate_meals_animation
        step.contains("stir", ignoreCase = true) -> R.raw.stirring_animation
        else -> null // Default: no animation
    }
}

fun extractTimeInSeconds(step: String): Long {
    val regex = Regex("(\\d+)\\s*minute")
    val match = regex.find(step)
    return match?.groupValues?.get(1)?.toLong()?.times(60) ?: 0L
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CookAlongScreenPreview() {
    // Mock instructions
    val mockInstructions = listOf(
        Instruction("Preheat the oven to 200°C.", "5 mins"),
        Instruction("Cut the vegetables into bite-sized pieces.", "10 mins"),
        Instruction("Mix the ingredients in a bowl.", "5 mins"),
        Instruction("Put the mixture in the oven and bake for 30 minutes.", "30 mins"),
        Instruction("Let the dish cool for 10 minutes before serving.", "10 mins")
    )

    // Preview CookAlongScreen
    CookAlongScreen(
        instructions = mockInstructions,
        onBack = {} // No-op for preview
    )
}
