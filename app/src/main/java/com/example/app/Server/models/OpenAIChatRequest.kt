package com.example.app.Server.models

data class OpenAIChatRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)
