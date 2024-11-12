package com.example.app.UIUX.screens

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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
                    // Navigate to recipe generation screen
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
