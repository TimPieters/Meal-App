package com.example.app.Server.models

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ReplyMessage  // Renamed to ReplyMessage for clarity
)

data class ReplyMessage(
    val role: String,
    val content: String  // Assuming content in response is always a string
)
