package com.example.app.Server.models

data class OpenAIChatRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: List<Content>  // List of Content objects to support multiple types
)

sealed class Content {
    data class Text(val type: String = "text", val text: String) : Content()
    data class ImageUrl(val type: String = "image_url", val image_url: ImageData) : Content()
}

data class ImageData(val url: String)
