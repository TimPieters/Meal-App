package com.example.app

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.example.app.ui.theme.PlayfulFontFamily
import java.util.Objects
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.Navigation.Screen


@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF687b76),
                        Color(0xFF394b4a)
                    )
                )
            )
    ) {
        content()
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
fun StandardizedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9eb1b8)),
        modifier = modifier
            .padding(16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(36.dp)
            ),
        shape = RoundedCornerShape(36.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            color = Color.White,
            fontFamily = PlayfulFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TopBar(
    onBackClicked: () -> Unit,
    profilePicturePainter: Painter,
    onProfileClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF9eb1b8)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onProfileClicked) {
            Image(
                painter = profilePicturePainter,
                contentDescription = "Profile",
                modifier =
                Modifier
                    .clip(CircleShape)
                    .size(48.dp)
                    .border(2.dp, Color.White, CircleShape) // Adds a white border if needed

            )
        }
    }
}

@Composable
fun ProgressBar(progress: Float) {
    LinearProgressIndicator(
        progress = progress.coerceIn(0f, 1f), // Ensures progress is within the valid range
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = Color.White,
    )
}

// A function that sets up the necessary components for capturing an image
@Composable
fun setupImageCapture(
    context: Context,
    onImageCaptured: (Uri) -> Unit
): () -> Unit {
    // State to store the captured image URI
    val capturedImageUri = remember { mutableStateOf<Uri?>(null) }

    // Prepare the file and URI for the image capture
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )

    // Launchers for taking a picture and requesting permission
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedImageUri.value?.let(onImageCaptured)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Check for camera permission when the composable is first launched
    LaunchedEffect(key1 = true) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Return a lambda that can be called to initiate the image capture process
    return {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            capturedImageUri.value = uri // Update the state with the new URI
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}



@Composable
fun ProcessRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProcessStep(
            icon = R.drawable.ic_camera, // Replace with your camera icon resource ID
            title = "1. Take a picture"
        )
        ProcessStep(
            icon = R.drawable.ic_ai, // Replace with your AI analysis icon resource ID
            title = "2. Analysis by AI"
        )
        ProcessStep(
            icon = R.drawable.ic_recipe, // Replace with your meal/checkmark icon resource ID
            title = "3. Get Recipes"
        )
    }
}

@Composable
fun ProcessStep(icon: Int, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp) // Size of the circle

                .border(1.dp, Color.Black, CircleShape) // Black border, adjust width as needed
                .border(width = 2.dp, color = Color.White, shape = CircleShape) // Black border for the circle
                .padding(4.dp) // Padding inside the circle
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.
                    clip(CircleShape) // Ensure the image itself is also clipped to circle shape
            )
        }
        Spacer(modifier = Modifier.height(4.dp)) // Space between icon and text
        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = PlayfulFontFamily,
            color = Color.White,
        )
    }
}

@Composable
fun CameraScreenBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val sharedViewModel: SharedViewModel = viewModel(LocalContext.current as ComponentActivity)


    Box(
        modifier = modifier
    ) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = Color(0xFF9eb1b8),
        shadowElevation = 4.dp
        ) {

    }
        // Call the setupImageCapture function and pass the lambda to the Button's onClick
        val context = LocalContext.current
        val onImageCaptured: (Uri) -> Unit = {uri ->
            sharedViewModel.setImageUri(uri)
            navController.navigate(Screen.ImagePreviewScreen.route)
        }
        val initiateCapture = setupImageCapture(context, onImageCaptured)


        Button(
            onClick = initiateCapture,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-16).dp) // Negative offset to make the button pop out
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50), // Circular button
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp) // Adds elevation to the button
        )
        {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_transparant),
                contentDescription = "Capture",
                modifier = Modifier.size(24.dp) // Adjust size as needed
            )
            Spacer(Modifier.width(8.dp)) // Spacing between icon and text
            Text(
                text = "Capture Image From Camera",
                modifier = Modifier.padding(start = 8.dp),
                fontFamily = PlayfulFontFamily,
                color = Color.Black,

            )

        }

    }
}


