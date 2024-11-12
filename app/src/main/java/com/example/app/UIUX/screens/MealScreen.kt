package com.example.app.UIUX.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.SharedViewModel
import com.example.app.Navigation.Screen

@Composable
fun MealScreen(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val meals by sharedViewModel.generatedRecipes.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Generated Meals", fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))

        meals.forEachIndexed { index, meal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.RecipeScreen.createRoute(index))
                    }
                    .padding(vertical = 4.dp)
            ) {
                // Display only the name of the meal
                Text(
                    text = meal.name,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
}
