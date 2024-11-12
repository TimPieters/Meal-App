package com.example.app.Server

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.Server.models.OpenAIResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import android.content.Context
import android.net.Uri

class OpenAIViewModel : ViewModel() {
    private val repository = OpenAIRepository()

    fun testConnection(apiKey: String, prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<OpenAIResponse> = repository.sendChatToOpenAI(apiKey, prompt)
                if (response.isSuccessful) {
                    // Extracting content from choices[0].message.content
                    val reply = response.body()?.choices?.get(0)?.message?.content ?: "No response"
                    Log.d("OpenAITest", "Success: $reply")
                    onResult("Success: $reply")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("OpenAITest", "API Error: $errorMessage")
                    onResult("Error: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("OpenAITest", "Exception: ${e.localizedMessage}")
                onResult("Exception: ${e.localizedMessage}")
            }
        }
    }

    fun analyzeImage(apiKey: String, imageUri: Uri, context: Context, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<OpenAIResponse>? = repository.sendImageToOpenAI(apiKey, imageUri, context)
                response?.let {
                    if (it.isSuccessful) {
                        val reply = it.body()?.choices?.get(0)?.message?.content as? String ?: "No response available"
                        Log.d("OpenAIApp", "API Success: $reply")
                        onResult(reply)
                    } else {
                        val errorMessage = it.errorBody()?.string() ?: "Unknown error"
                        Log.e("OpenAIApp", "API Error: $errorMessage")
                        onResult("Error: $errorMessage")
                    }
                } ?: run {
                    Log.e("OpenAIApp", "Error: Failed to encode image")
                    onResult("Error: Failed to encode image")
                }
            } catch (e: Exception) {
                Log.e("OpenAIApp", "Exception occurred: ${e.localizedMessage}")
                onResult("Exception: ${e.localizedMessage}")
            }
        }
    }

}
