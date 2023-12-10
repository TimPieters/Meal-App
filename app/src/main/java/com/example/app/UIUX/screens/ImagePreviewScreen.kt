package com.example.app.UIUX.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.SharedViewModel

@Preview
@Composable
fun ImagePreviewScreen(viewModel: SharedViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val capturedImageUri by viewModel.capturedImageUri.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Display the image if it's available
        capturedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Preview Image",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Add a button for submitting the image or navigating back
        Button(
            onClick = {
                // Handle the submission or navigation
                // For example, to navigate back to the CameraScreen:
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit")
        }
    }
}