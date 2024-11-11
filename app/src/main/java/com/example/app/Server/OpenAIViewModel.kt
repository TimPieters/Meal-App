package com.example.app.Server

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.Server.models.OpenAIResponse
import kotlinx.coroutines.launch
import retrofit2.Response

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

}
