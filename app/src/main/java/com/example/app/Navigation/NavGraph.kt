package com.example.app.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.SharedViewModel
import com.example.app.UIUX.screens.CameraScreen
import com.example.app.UIUX.screens.ImagePreviewScreen

sealed class Screen(val route: String) {
    object CameraScreen : Screen("camera_screen")
    object ImagePreviewScreen : Screen("image_preview_screen/{imageUri}") {
        fun createRoute(imageUri: String) = "image_preview_screen/$imageUri"
    }
}
@Composable
fun NavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(navController, startDestination = Screen.CameraScreen.route) {
        composable(Screen.CameraScreen.route) {
            CameraScreen(navController, sharedViewModel)
        }
        composable(
            Screen.ImagePreviewScreen.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            ImagePreviewScreen(
                viewModel = sharedViewModel,
                navController = navController
            )
        }
    }
}