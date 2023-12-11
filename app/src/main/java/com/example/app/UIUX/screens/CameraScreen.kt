package com.example.app.UIUX.screens

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.app.CameraScreenBottomBar
import com.example.app.GradientBackground
import com.example.app.Navigation.Screen
import com.example.app.ProcessRow
import com.example.app.R
import com.example.app.SharedViewModel
import com.example.app.TopBar
import com.example.app.fadingEdge
import com.example.app.ui.theme.PlayfulFontFamily


val topBottomFade = Brush.verticalGradient(0f to Color.Transparent, 0.2f to Color.Red, 0.8f to Color.Red, 1f to Color.Transparent)

@Composable
fun CameraScreen(navController: NavHostController) {

    GradientBackground {
        Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {TopBar(
            onBackClicked = {},
            profilePicturePainter = painterResource(id = R.drawable.topbarimage_placeholder), // Replace with an actual image resource
            onProfileClicked = {})

            Spacer(modifier = Modifier.height(32.dp)) // Adjust the space as needed

        Image(
            painter = painterResource(id = R.drawable.fridge_camera_screen),
            contentDescription = "Fridge picture",
            modifier = Modifier
                .size(300.dp) // Set the size of the circle
                .border(6.dp, Color.White, CircleShape) // White border, adjust width as needed
                .padding(6.dp) // Space for the black border, making the white border visible
                .border(6.dp, Color.Black, CircleShape) // Black border, adjust width as needed
                .clip(CircleShape), // Clip the image to a circle
            contentScale = ContentScale.Crop // Crop the image to fill the circle
        )
            Spacer(modifier = Modifier.height(32.dp)) // Adjust the space as needed

            Box(
                contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(50)) // Oval shape
                .padding(horizontal = 20.dp, vertical = 10.dp) // Padding inside the box

            ) {
                Text(
                    "Take a picture of your fridge!",
                    color = Color.Black,
                    fontFamily = PlayfulFontFamily,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Adjust the space as needed

            ProcessRow()


    }
            CameraScreenBottomBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                navController = navController
            )
    }}


}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    val fakeNavController = rememberNavController()
    val fakeViewModel = SharedViewModel() // Assuming default constructor available

    CameraScreen(navController = fakeNavController)
}
