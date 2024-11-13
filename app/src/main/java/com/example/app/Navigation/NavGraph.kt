package com.example.app.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.SharedViewModel
import com.example.app.UIUX.screens.OnboardingScreen
import com.example.app.UIUX.screens.CameraScreen
import com.example.app.UIUX.screens.ImagePreviewScreen
import com.example.app.UIUX.screens.IngredientScreen
import com.example.app.UIUX.screens.MealScreen
import com.example.app.UIUX.screens.RecipeScreen

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object CameraScreen : Screen("camera_screen")
    object ImagePreviewScreen : Screen("image_preview_screen")
    object IngredientScreen : Screen("ingredient_screen") // New route for IngredientScreen
    object MealScreen : Screen("meal_screen")
    object RecipeScreen : Screen("recipe_screen/{mealId}") {
        fun createRoute(mealId: Int) = "recipe_screen/$mealId"
    }
}

@Composable
fun NavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(navController, startDestination = Screen.OnboardingScreen.route) {
        composable(Screen.OnboardingScreen.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.CameraScreen.route) {
                        popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.CameraScreen.route) {
            CameraScreen(navController)
        }
        composable(Screen.ImagePreviewScreen.route) {
            ImagePreviewScreen(navController)
        }
        composable(Screen.IngredientScreen.route) {
            IngredientScreen(navController, sharedViewModel) // Pass sharedViewModel to IngredientScreen
        }
        composable(Screen.MealScreen.route) {
            MealScreen(navController, sharedViewModel)
        }
        composable(Screen.RecipeScreen.route) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId")?.toInt() ?: -1
            RecipeScreen(mealId, navController, sharedViewModel) // Pass both navController and sharedViewModel
        }
    }
}
