package com.example.app.UIUX.screens

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.SharedViewModel
import com.example.app.TopBar


@Composable
fun ImagePreviewScreen(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel(LocalContext.current as ComponentActivity)
    val capturedImageUri by sharedViewModel.capturedImageUri.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            onBackClicked = {navController.popBackStack()},
            profilePicturePainter = painterResource(id = R.drawable.topbarimage_placeholder), // Replace with an actual image resource
            onProfileClicked = {})
        // Display the image if it's available
        capturedImageUri?.let { uri ->
            Log.d("ImagePreviewScreen", "URI: $uri")
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = "Preview Image",
                modifier = Modifier.
                fillMaxWidth()
                    .aspectRatio(1f)
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
@Preview(showBackground = true)
@Composable
fun ImagePreviewScreenPreview() {
    val fakeNavController = rememberNavController()
    val fakeViewModel = SharedViewModel() // Provide necessary default data

    ImagePreviewScreen(navController = fakeNavController)
}