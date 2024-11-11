package com.example.app.Server

import com.example.app.Server.models.OpenAIRequest
import com.example.app.Server.models.OpenAIChatRequest
import com.example.app.Server.models.OpenAIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") authorization: String,
        @Body request: OpenAIChatRequest
    ): Response<OpenAIResponse>
}
