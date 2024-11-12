package com.example.app.Server

import com.example.app.Server.RetrofitInstance
import com.example.app.Server.models.OpenAIRequest
import com.example.app.Server.models.OpenAIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

import com.example.app.Server.models.OpenAIChatRequest
import com.example.app.Server.models.Message
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.app.Server.models.ImageData
import java.io.InputStream
import com.example.app.Server.models.Content

class OpenAIRepository {
    fun encodeImageToBase64(context: Context, imageUri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            bytes?.let { Base64.encodeToString(it, Base64.NO_WRAP) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun logImageAspectRatio(context: Context, imageUri: Uri) {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        val width = options.outWidth
        val height = options.outHeight
        Log.d("OpenAIApp", "Image Aspect Ratio - Width: $width px, Height: $height px")
    }

    private fun logTokenUsage(response: Response<OpenAIResponse>?) {
        response?.body()?.usage?.let { usage ->
            Log.d("OpenAIApp", "Token usage for this call - Prompt: ${usage.prompt_tokens}, Completion: ${usage.completion_tokens}, Total: ${usage.total_tokens}")
        } ?: Log.d("OpenAIApp", "Token usage information not available in response")
    }

    suspend fun sendImageToOpenAI(apiKey: String, imageUri: Uri, context: Context): Response<OpenAIResponse>? {
        // Log the image aspect ratio
        logImageAspectRatio(context, imageUri)

        val base64Image = encodeImageToBase64(context, imageUri)
        base64Image?.let {
            val imageData = ImageData(url = "data:image/jpeg;base64,$it")
            val request = OpenAIChatRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    Message(
                        role = "system",
                        content = listOf(Content.Text(text = "Please describe this image."))
                    ),
                    Message(
                        role = "user",
                        content = listOf(
                            Content.Text(text = "Describe this art style."),
                            Content.ImageUrl(image_url = imageData)
                        )
                    )
                )
            )
            return withContext(Dispatchers.IO) {
                val response = RetrofitInstance.api.getCompletion("Bearer $apiKey", request)
                // Log the token usage after receiving the response
                logTokenUsage(response)
                return@withContext response
            }
        }
        return null
    }

    suspend fun sendChatToOpenAI(apiKey: String, content: String): Response<OpenAIResponse> {
        val request = OpenAIChatRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(
                    role = "user",
                    content = listOf(Content.Text(text = content))  // Wrap content as List<Content>
                )
            )
        )

        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getCompletion("Bearer $apiKey", request)
        }
    }
}
