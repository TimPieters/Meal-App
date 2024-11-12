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

class SharedViewModel : ViewModel() {
    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri

    // For storing and managing detected ingredients
    private val _ingredients = MutableLiveData<MutableList<String>>(mutableListOf())
    val ingredients: LiveData<MutableList<String>> get() = _ingredients

    private val openAIRepository = OpenAIRepository()

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
}
