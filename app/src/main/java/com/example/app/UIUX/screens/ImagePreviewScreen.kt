package com.example.app.UIUX.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.app.FramedImage
import com.example.app.GradientBackground
import com.example.app.ProgressBar
import com.example.app.R
import com.example.app.SegmentedProgressBar
import com.example.app.SharedViewModel
import com.example.app.StandardizedButton
import com.example.app.TopBar


@Composable
fun ImagePreviewScreen(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel(LocalContext.current as ComponentActivity)
    val context = LocalContext.current // Get context here
    val capturedImageUri by sharedViewModel.capturedImageUri.observeAsState()
    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                onBackClicked = { navController.popBackStack() },
                profilePicturePainter = painterResource(id = R.drawable.topbarimage_placeholder), // Replace with an actual image resource
                onProfileClicked = {})
            Spacer(modifier = Modifier.height(12.dp)) // Increase the height as needed for more space
            SegmentedProgressBar(currentStep = 1, totalSteps = 3)
            Spacer(modifier = Modifier.height(12.dp)) // Increase the height as needed for more space
            // Display the image if it's available
            capturedImageUri?.let { uri ->
                FramedImage(
                    imageUri = uri,
                    contentDescription = "Preview Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp) // Add padding to create a margin around the frame
                )
            }

            // Add a button for submitting the image or navigating back
            StandardizedButton(
                text = "Submit",
                onClick = {
                    capturedImageUri?.let { uri ->
                        sharedViewModel.submitImageToOpenAI(uri, context)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ImagePreviewScreenPreview() {
    val fakeNavController = rememberNavController()
    val fakeViewModel = SharedViewModel() // Provide necessary default data

    ImagePreviewScreen(navController = fakeNavController)
}