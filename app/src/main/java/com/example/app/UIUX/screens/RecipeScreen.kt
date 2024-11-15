package com.example.app.UIUX.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import com.example.app.Recipe

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

        // Tab state to toggle between "Ingredients" and "Steps"
        var selectedTab by remember { mutableStateOf("Ingredients") }

        meal?.let {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recipe Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )

                // Dynamic Content based on selected tab
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item{
                        Spacer(modifier = Modifier.height(16.dp))
                        // Display BasicInfo at the top of the screen
                        BasicInfo(meal = it)
                        Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item{
                        // Tab Row for Ingredients and Steps
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("Ingredients", "Steps").forEach { tab ->
                                TabButton(
                                    text = tab,
                                    active = selectedTab == tab,
                                    onClick = { selectedTab = tab }
                                )
                            }
                        }
                    }
                    when (selectedTab) {
                        "Ingredients" -> {
                            item {
                                Text(
                                    "Ingredients",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(it.ingredients) { ingredient ->
                                Text(
                                    "- $ingredient",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                        "Steps" -> {
                            item {
                                Text(
                                    "Instructions",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            itemsIndexed(it.instructions) { index, instruction ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Step ${index + 1}",
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
                        }
                    }


                }
            }
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Recipe not found", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TabButton(text: String, active: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BasicInfo(meal: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 12.dp) // Inner padding for spacing within the bar
    ) {
        InfoColumn(R.drawable.ic_clock, meal.estimated_total_time, "Time")
        InfoColumn(R.drawable.ic_difficulty, meal.difficulty.capitalize(), "Difficulty")
    }
}

@Composable
fun InfoColumn(
    @DrawableRes iconResource: Int,
    text: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
    }
}

