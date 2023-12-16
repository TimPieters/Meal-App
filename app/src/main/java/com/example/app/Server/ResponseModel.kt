package com.example.app.Server

data class ResponseModel(
    val id: String?,
    val `object`: String?,
    val created: Int?,
    val model: String?,
    val choices: List<Choice>?
)

data class Choice(
    val message: Message?
)

data class Message(
    val role: String?,
    val content: Content?
)

data class Content(
    val type: String?,
    val text: String?,
    val image_url: ImageUrl?
)

data class ImageUrl(
    val url: String?
)