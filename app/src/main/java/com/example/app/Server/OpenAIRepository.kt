package com.example.app.Server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenAIRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(OpenAIApiService::class.java)

    suspend fun analyzeImage(base64Image: String): Map<String, Any> {
        val payload = mapOf(
            "model" to "gpt-4-vision-preview",
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to "What’s in this fridge? Write it in bulletpoints."
                        ),
                        mapOf(
                            "type" to "image_url",
                            "image_url" to mapOf(
                                "url" to "data:image/jpeg;base64,$base64Image"
                            )
                        )
                    )
                )
            ),
            "max_tokens" to 300
        )

        return apiService.analyzeImage(payload)
    }
}
