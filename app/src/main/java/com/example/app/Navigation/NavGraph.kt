package com.example.app.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.SharedViewModel
import com.example.app.UIUX.screens.CameraScreen
import com.example.app.UIUX.screens.ImagePreviewScreen

sealed class Screen(val route: String) {
    object CameraScreen : Screen("camera_screen")
    object ImagePreviewScreen : Screen("image_preview_screen")

}
@Composable
fun NavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(navController, startDestination = Screen.CameraScreen.route) {
        composable(Screen.CameraScreen.route) {
            CameraScreen(navController)
        }
        composable(Screen.ImagePreviewScreen.route){
            ImagePreviewScreen(navController)
        }
    }
}