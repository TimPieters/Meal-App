package com.example.app.UIUX.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.app.Navigation.Screen
import com.example.app.R
import com.example.app.SharedViewModel

@Composable
fun IngredientScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val ingredients by sharedViewModel.ingredients.observeAsState(emptyList())
    var newIngredient by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var ingredientToEdit by remember { mutableStateOf("") }
    var editedIngredient by remember { mutableStateOf("") }
    val isGeneratingRecipes by sharedViewModel.isGeneratingRecipes.observeAsState(false)
    // Loading Screen
    AnimatedVisibility(
        visible = isGeneratingRecipes,
        enter = fadeIn(animationSpec = tween(durationMillis = 700, easing = LinearEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 700, easing = LinearEasing))
    ) {
        MealLoadingScreen()
    }

    // Main Content
    AnimatedVisibility(
        visible = !isGeneratingRecipes,
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // Ensure button at bottom is visible
        ) {
            item {
                Text(
                    text = "Detected Ingredients",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(ingredients.size) { index ->
                val ingredient = ingredients[index]
                IngredientCard(
                    ingredient = ingredient,
                    onEditClick = {
                        ingredientToEdit = ingredient
                        editedIngredient = ingredient
                        showEditDialog = true
                    },
                    onDeleteClick = { sharedViewModel.removeIngredient(ingredient) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Input field for adding new ingredients
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = newIngredient,
                        onValueChange = { newIngredient = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        decorationBox = { innerTextField ->
                            if (newIngredient.isEmpty()) {
                                Text(
                                    text = "Add a new ingredient...",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newIngredient.isNotBlank()) {
                                sharedViewModel.addIngredient(newIngredient)
                                newIngredient = ""
                            }
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                // Confirm button positioned at the bottom
                Button(
                    onClick = {
                        val apiKey =
                            ""  // Replace with your actual OpenAI API key
                        sharedViewModel.setIsGeneratingRecipes(true)
                        sharedViewModel.generateRecipes(apiKey, ingredients) { success ->
                            sharedViewModel.setIsGeneratingRecipes(false)
                            if (success) {
                                navController.navigate(Screen.MealScreen.route)
                            } else {
                                // Handle error (e.g., show a toast or alert)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Confirm Ingredients",
                        fontSize = 18.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Edit ingredient dialog
        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Ingredient") },
                text = {
                    TextField(
                        value = editedIngredient,
                        onValueChange = { editedIngredient = it },
                        placeholder = { Text("Enter new ingredient name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        sharedViewModel.updateIngredient(ingredientToEdit, editedIngredient)
                        showEditDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun IngredientCard(
    ingredient: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the image if available, otherwise show placeholder
            val imageRes = getImageResourceForIngredient(ingredient)
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = ingredient,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Delete, // Use a generic icon as a placeholder
                    contentDescription = "Ingredient Placeholder",
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = ingredient, fontSize = 18.sp)
            Row {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun getImageResourceForIngredient(ingredient: String): Int? {
    return when (ingredient.lowercase()) {
        // Cooking stuff
        "salt" -> R.drawable.salt
        "pepper" -> R.drawable.salt
        "olive oil" -> R.drawable.olive_oil
        "garlic" -> R.drawable.garlic
        "butter" -> R.drawable.butter

        // Dry
        "pasta","penne","tagliatelle","gnocchi" -> R.drawable.pasta
        "rice" -> R.drawable.rice
        "seeds", "sesame seeds", "chia seeds", "sunflower seeds", "pumpkin seeds" -> R.drawable.seeds
        "tortilla","tortillas" -> R.drawable.tortillas
        "nuts","almonds","peanuts" -> R.drawable.nuts
        "flour" -> R.drawable.flour
        "sugar" -> R.drawable.sugar

        // Dairy
        "cheese" -> R.drawable.cheese
        "milk" -> R.drawable.milk
        "yogurt","yoghurt" -> R.drawable.yoghurt
        "cream cheese" -> R.drawable.cream_cheese

        // Sauce
        "ketchup","mayonnaise","mustard" -> R.drawable.sauces
        "maple syrup" -> R.drawable.maple_syrup
        "tomato sauce" -> R.drawable.tomato_sauce
        "soy sauce" -> R.drawable.soy_sauce
        "pesto","pesto sauce" -> R.drawable.pesto

        // Other
        "egg","eggs" -> R.drawable.egg
        "honey" -> R.drawable.honey
        "chocolate" -> R.drawable.chocolate

        // Vegetables
        "onion","onions" -> R.drawable.onion
        "tomato","tomatoes" -> R.drawable.tomato
        "potato","potatoes" -> R.drawable.potato
        "carrot","carrots" -> R.drawable.carrot
        "cabbage","lettuce" -> R.drawable.cabbage
        "red cabbage" -> R.drawable.red_cabbage
        "cauliflower" -> R.drawable.cauliflower
        "green beans" -> R.drawable.green_beans
        "asparagus" -> R.drawable.asparagus
        "brussels sprouts","sprouts" -> R.drawable.brussels_sprouts
        "artichoke" -> R.drawable.artichoke
        "mushroom","mushrooms" -> R.drawable.mushrooms
        "eggplant" -> R.drawable.eggplant
        "broccoli" -> R.drawable.broccoli
        "bell pepper","bellpepper", "bell peppers", "red pepper", "red peppers", "green pepper", "green peppers", "yellow pepper", "yellow peppers", "red bell pepper", "green bell pepper","yellow bell pepper" -> R.drawable.bell_pepper
        "cucumber","cucumbers" -> R.drawable.cucumber
        "pickle","pickles" -> R.drawable.pickle
        "leafy greens","greens","spinach","arugula" -> R.drawable.greens
        "basil","cilantro","mint" -> R.drawable.basil

        // Fruit
        "lime","limes" -> R.drawable.lime
        "lemon","lemons" -> R.drawable.lemon
        "pear","pears" -> R.drawable.pear
        "orange","oranges" -> R.drawable.orange
        "apple","apples","green apples" -> R.drawable.apple
        "banana","bananas" -> R.drawable.banana
        "date","dates","dried dates" -> R.drawable.dates
        "strawberry","strawberries" -> R.drawable.strawberry

        // Meat
        "chicken breast", "chicken", "meat", "steak","pork meat", "lambchops", "fish" -> R.drawable.meat
        else -> R.drawable.ingredients
    }
}
