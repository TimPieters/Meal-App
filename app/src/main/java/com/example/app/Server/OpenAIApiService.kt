package com.example.app.Server

import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Base64
import retrofit2.http.Headers
import java.io.File

fun encodeImageToBase64(imagePath: String): String {
    val bytes = File(imagePath).readBytes()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
interface OpenAIApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun analyzeImage(@Body payload: Map<String, Any>): Map<String, Any>
}