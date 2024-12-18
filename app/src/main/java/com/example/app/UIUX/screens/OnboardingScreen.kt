package com.example.app.UIUX.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.app.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit = {}) {
    val pages = listOf(
        OnboardingPage(
            title = "Take a picture of your Fridge",
            description = "Capture a photo of your fridge so our AI can identify the ingredients.",
            animationRes = R.raw.camera_animation
        ),
        OnboardingPage(
            title = "Let AI generate Meals",
            description = "Our AI will create a variety of meal options to make the most out of your ingredients.",
            animationRes = R.raw.generate_meals_animation
        ),
        OnboardingPage(
            title = "Start Cooking!",
            description = "Follow the step-by-step instructions for a delicious meal, no matter your cooking level.",
            animationRes = R.raw.cooking_animation
        )
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Horizontal pager for swiping through pages
        HorizontalPager(
            state = pagerState,
            count = pages.size,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        // Indicator dots directly under the animation and title
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { index ->
                IndicatorSingleDot(isSelected = pagerState.currentPage == index)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation buttons at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            if (pagerState.currentPage > 0) {
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Next/Get Started button
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            onFinish()
                        }
                    }
                }
            ) {
                Text(text = if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started")
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp) // This padding pushes the entire content a bit downward
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(page.animationRes))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        // Lottie animation, placed a bit higher on the screen
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp)
        )

        // Page title
        Text(
            text = page.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Page description
        Text(
            text = page.description,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFE92F1E) else Color(0x25E92F1E))
    )
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val animationRes: Int
)



@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}


