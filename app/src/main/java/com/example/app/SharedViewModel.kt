package com.example.app

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.Server.OpenAIRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Recipe(
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<Instruction>, // Use Instruction class for detailed steps
    val estimated_total_time: String,
    val difficulty: String,
    val serving_size: Int,
    val nutritional_info: String
)

data class Instruction(
    val step: String,
    val approximate_time: String
)


open class SharedViewModel(
    initialRecipes: List<Recipe> = emptyList() // Default to an empty list
) : ViewModel() {
    private val _generatedRecipes = MutableLiveData(initialRecipes)
    val generatedRecipes: LiveData<List<Recipe>> get() = _generatedRecipes
    fun setGeneratedRecipes(recipes: List<Recipe>) {
        _generatedRecipes.value = recipes
    }

    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri

    // For storing and managing detected ingredients
    private val _ingredients = MutableLiveData<MutableList<String>>(mutableListOf())
    val ingredients: LiveData<MutableList<String>> get() = _ingredients

    private val _isDetectingIngredients =
        MutableLiveData(false) // New loading state for ingredient detection
    val isDetectingIngredients: LiveData<Boolean> get() = _isDetectingIngredients

    // In SharedViewModel.kt
    private val _isGeneratingRecipes = MutableLiveData(false)
    val isGeneratingRecipes: LiveData<Boolean> get() = _isGeneratingRecipes

    private val openAIRepository = OpenAIRepository()

    fun setIsDetectingIngredients(value: Boolean) {
        _isDetectingIngredients.value = value
        Log.d("SharedViewModel", "Is detecting ingredients set to: $value")
    }

    fun setIsGeneratingRecipes(value: Boolean) {
        _isGeneratingRecipes.value = value
        Log.d("SharedViewModel", "Is generating recipes set to: $value")
    }

    // Set the captured image URI
    fun setImageUri(uri: Uri?) {
        Log.d("SharedViewModel", "Image URI set: $uri")
        _capturedImageUri.value = uri
    }

    // Set the initial list of detected ingredients from AI response
    fun setDetectedIngredients(ingredientList: List<String>) {
        _ingredients.value = ingredientList.toMutableList()
        Log.d("SharedViewModel", "Detected ingredients set: $ingredientList")
    }

    fun addIngredient(ingredient: String) {
        _ingredients.value = _ingredients.value?.toMutableList()?.apply {
            add(ingredient)
            Log.d("SharedViewModel", "Ingredient added: $ingredient")
        }
    }

    fun removeIngredient(ingredient: String) {
        _ingredients.value = _ingredients.value?.toMutableList()?.apply {
            remove(ingredient)
            Log.d("SharedViewModel", "Ingredient removed: $ingredient")
        }
    }

    fun updateIngredient(oldIngredient: String, newIngredient: String) {
        _ingredients.value = _ingredients.value?.toMutableList()?.apply {
            val index = indexOf(oldIngredient)
            if (index != -1) {
                set(index, newIngredient)  // Update the ingredient directly at its index
            }
        }
        Log.d("SharedViewModel", "Ingredient updated: $oldIngredient -> $newIngredient")
    }


    fun generateRecipes(apiKey: String, ingredients: List<String>, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d("SharedViewModel", "Initiating recipe generation with ingredients: $ingredients")
            try {
                val response = openAIRepository.generateRecipes(
                    apiKey,
                    ingredients,
                    "Any",
                    "Dinner",
                    "Easy to Medium",
                    2,
                    "Low calories, high protein"
                )
                response?.let {
                    if (it.isSuccessful) {
                        val responseBody = it.body()
                        val responseText = responseBody?.choices?.get(0)?.message?.content ?: ""
                        val recipes = parseRecipes(responseText)
                        _generatedRecipes.value = recipes // Set as List<Recipe>
                        Log.d(
                            "SharedViewModel",
                            "Recipes generated successfully. Total recipes: ${recipes.size}"
                        )
                        recipes.forEachIndexed { index, recipe ->
                            Log.d("SharedViewModel", "Recipe $index: ${recipe.name}")
                        }
                        onResult(true)
                    } else {
                        Log.e("SharedViewModel", "Failed with status code: ${it.code()}")
                        Log.e(
                            "SharedViewModel",
                            "Error details: ${it.errorBody()?.string() ?: "No error body"}"
                        )
                        onResult(false)
                    }
                } ?: run {
                    Log.e("SharedViewModel", "Null response received from OpenAI API.")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e(
                    "SharedViewModel",
                    "Exception during recipe generation: ${e.localizedMessage}"
                )
                onResult(false)
            }
        }
    }

    private fun parseRecipes(responseText: String): List<Recipe> {
        val cleanedText = responseText
            .trim()
            .removeSurrounding("```json", "```")
            .removeSurrounding("```", "```")

        return try {
            val recipeListType = object : TypeToken<List<Recipe>>() {}.type
            Gson().fromJson<List<Recipe>>(cleanedText, recipeListType)
        } catch (e: Exception) {
            Log.e("SharedViewModel", "Failed to parse recipes: ${e.localizedMessage}")
            emptyList()
        }
    }
}





