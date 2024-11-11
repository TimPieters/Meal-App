package com.example.app.Server.models

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)
