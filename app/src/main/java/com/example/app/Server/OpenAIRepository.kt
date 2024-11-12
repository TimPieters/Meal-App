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
        // Reference a stock image for testing
        val image = Uri.parse("android.resource://com.example.app/drawable/stock_fridge")

        // Log the image aspect ratio
        logImageAspectRatio(context, image )

        val base64Image = encodeImageToBase64(context, image )
        base64Image?.let {
            val imageData = ImageData(url = "data:image/jpeg;base64,$it")
            val request = OpenAIChatRequest(
                model = "gpt-4o-mini",
                messages = listOf(
                    Message(
                        role = "system",
                        content = listOf(Content.Text(text = "Give a list of ingredients seen in a fridge image that can be used for cooking. You always seperate it with a comma delimiter. The first letter of the word should always be capitalized."
                        ))
                    ),
                    Message(
                        role = "user",
                        content = listOf(
                            Content.Text(text = "Please give me a list of ingredients from the fridge in the image."),
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
