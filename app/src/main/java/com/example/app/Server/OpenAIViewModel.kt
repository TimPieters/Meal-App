package com.example.app.Server

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OpenAIViewModel(private val repository: OpenAIRepository) : ViewModel() {
    fun analyzeImage(imagePath: String) {
        viewModelScope.launch {
            val base64Image = encodeImageToBase64(imagePath)
            val response = repository.analyzeImage(base64Image)
            // Handle the response
        }
    }
}
