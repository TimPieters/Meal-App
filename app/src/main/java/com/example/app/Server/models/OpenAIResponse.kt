package com.example.app.Server.models

data class OpenAIResponse(
    val choices: List<Choice>,
    val usage: Usage?  // Optional field for usage info
)

data class Choice(
    val message: ReplyMessage
)

data class ReplyMessage(
    val role: String,
    val content: String
)

data class Usage(  // New data class to hold usage information
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
