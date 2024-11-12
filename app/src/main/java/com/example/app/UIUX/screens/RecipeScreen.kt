package com.example.app.UIUX.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.SharedViewModel
import com.example.app.TopBar
import com.example.app.R

@Composable
fun RecipeScreen(mealId: Int, navController: NavHostController, sharedViewModel: SharedViewModel) {
    val meals by sharedViewModel.generatedRecipes.observeAsState(emptyList())
    val meal = meals.getOrNull(mealId)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Custom TopBar with back navigation
        TopBar(
            onBackClicked = { navController.popBackStack() },
            profilePicturePainter = painterResource(id = R.drawable.topbarimage_placeholder),
            onProfileClicked = {}
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            meal?.let {
                item {
                    Text("Recipe Details", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

                    Text("Recipe Name:", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Text(it.name, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp, bottom = 12.dp))

                    Text("Ingredients:", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                }

                items(it.ingredients) { ingredient ->
                    Text("- $ingredient", fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Instructions:", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                }

                itemsIndexed(it.instructions) { index, instruction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Step ${index + 1}:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = instruction.step,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "Approximate Time: ${instruction.approximate_time}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "Estimated Total Time: ${it.estimated_total_time}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Difficulty: ${it.difficulty.capitalize()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } ?: item {
                Text("Recipe not found", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
