package com.example.app.Server

import com.example.app.Server.RetrofitInstance
import com.example.app.Server.models.OpenAIRequest
import com.example.app.Server.models.OpenAIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

import com.example.app.Server.models.OpenAIChatRequest
import com.example.app.Server.models.Message

class OpenAIRepository {
    suspend fun sendChatToOpenAI(apiKey: String, content: String): Response<OpenAIResponse> {
        val request = OpenAIChatRequest(
            model = "gpt-4o-mini",
            messages = listOf(Message(role = "user", content = content))
        )

        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getCompletion("Bearer $apiKey", request)
        }
    }
}
