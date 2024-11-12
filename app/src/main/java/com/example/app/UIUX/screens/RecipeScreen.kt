package com.example.app.UIUX.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.SharedViewModel

@Composable
fun RecipeScreen(mealId: Int, sharedViewModel: SharedViewModel) {
    val meals by sharedViewModel.generatedRecipes.observeAsState(emptyList())
    val meal = meals.getOrNull(mealId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        meal?.let {
            Text("Recipe Details", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

            Text("Recipe Name: ${it.name}", fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))

            Text("Ingredients:", fontSize = 18.sp, modifier = Modifier.padding(top = 12.dp))
            it.ingredients.forEach { ingredient ->
                Text("- $ingredient", fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }

            Text("Instructions:", fontSize = 18.sp, modifier = Modifier.padding(top = 12.dp))
            it.instructions.forEachIndexed { index, instruction ->
                Text("${index + 1}. $instruction", fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            }
        } ?: run {
            Text("Recipe not found", fontSize = 18.sp)
        }
    }
}
