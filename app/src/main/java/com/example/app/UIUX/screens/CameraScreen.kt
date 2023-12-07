package com.example.app.UIUX.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.AppContent
import androidx.compose.ui.Modifier


@Preview
@Composable
fun CameraScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Use a basic color for the background
    ) {
        AppContent()
    }

}