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
        val image = Uri.parse("android.resource://com.example.app/drawable/stock_fridge3")

        // Log the image aspect ratio
        logImageAspectRatio(context, image )

        val base64Image = encodeImageToBase64(context, image )
        base64Image?.let {
            val imageData = ImageData(url = "data:image/jpeg;base64,$it")
            val request = OpenAIChatRequest(
                model = "gpt-4o",
                messages = listOf(
                    Message(
                        role = "system",
                        content = listOf(Content.Text(text =
                        """
                            Your task is to extract a **list of cooking ingredients** from a provided fridge image.
                            These ingredients will be used by a large language model to generate recipes, so please make them as accurate as possible.
                            You always try to name as much ingredients as possible. 
                            Look for fruits, vegetables, dairy, meat, sauces.
                            
                            Follow these rules strictly:
                            - **Separate all ingredients with commas**.
                            - **Capitalize the first letter** of each ingredient name.
                            - Do **not** add any trailing punctuation like periods, exclamation marks, etc.
                            - If an ingredient is detected multiple times, **list it only once**.
                            - Do **not** mention brand names.
                            - Use **common, standardized ingredient names** (e.g., "Cherry Tomato" -> "Tomato", "Dijon Mustard" -> "Mustard", "Penne" -> "Pasta", unless the distinction is clear and important).
                            - Return only the **list of ingredients** in a single-line format. Do not include any extra commentary or text outside the list.
                            """
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

    suspend fun generateRecipes(
        apiKey: String,
        ingredients: List<String>,
        cuisineType: String,
        mealType: String,
        difficultyLevel: String,
        servingSize: Int,
        nutritionalGoal: String
    ): Response<OpenAIResponse> {
        // Convert the ingredients list into a formatted string
        val ingredientsText = ingredients.joinToString(", ")
        // Log the ingredients and setup message
        Log.d("OpenAIRepository", "Initiating recipe generation with ingredients: $ingredientsText")
        // Set up the request messages
        val request = OpenAIChatRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                Message(
                    role = "system",
                    content = listOf(
                        Content.Text(
                            text = """
                            Please generate five detailed recipes using the following ingredients: $ingredientsText.
                            
                            The user prefers recipes from the "$cuisineType" cuisine, is looking for "$mealType" ideas, would like recipes for "$servingSize" people, and prefers recipes with a "$difficultyLevel" difficulty level.
                        
                            Additionally, the user has a nutritional goal of "$nutritionalGoal" (e.g., high protein, low fat, balanced nutrition). Tailor each recipe to match these dietary preferences, cuisine type, meal type, serving size, difficulty level, and nutritional goals.

                            Each recipe should be in the following JSON format, with specific details and approximate times for each step:
                            
                            {
                              "name": "Recipe Name",
                              "description": "A 3 sentence description of the meal",
                              "serving_size": "Number of servings, e.g., '4'",
                              "nutritional_info": "Brief nutritional summary, such as 'High protein, low fat'",
                              "ingredients": [
                              {"name": Name of the ingredient (e.g. Flour, Salt, Onion,...),
                              "quantity": Quantity of each ingredient (e.g., '2 cups', '1 tsp')"
                                },
                              ],
                              "instructions": [
                                {
                                  "step": "Detailed description of the step, including precise actions (e.g., 'Chop the carrots into 1/2-inch pieces')",
                                  "approximate_time": "Estimated time for this step in minutes (e.g., '5 minutes')"
                                },
                                {
                                  "step": "Next step with detailed instructions and approximate time"
                                }
                              ],
                              "estimated_total_time": "Total time for the recipe in minutes (e.g., '45 minutes')",
                              "difficulty": "Level of difficulty (easy, medium, hard)"
                            }
                            
                            Please ensure the response is strictly formatted as JSON without any additional text outside the JSON format.
                            Each recipe should contain detailed, clear steps with estimated times, a total time for the recipe, and a difficulty level.
                        """.trimIndent()
                        )
                    )
                ),
                Message(
                    role = "user",
                    content = listOf(Content.Text(text = "Please provide the recipes in the specified JSON format."))
                )
            )
        )
        Log.d("OpenAIRepository", "Request created with model: ${request.model} and messages: ${request.messages}")
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getCompletion("Bearer $apiKey", request)
                if (response.isSuccessful) {
                    // Log the successful response details
                    val responseBody = response.body()
                    Log.d("OpenAIRepository", "Response received successfully.")
                    Log.d("OpenAIRepository", "Response body: ${responseBody?.choices?.get(0)?.message?.content ?: "No content"}")
                } else {
                    // Log detailed error information
                    Log.e("OpenAIRepository", "Failed response with status code: ${response.code()}")
                    Log.e("OpenAIRepository", "Error body: ${response.errorBody()?.string() ?: "No error details"}")
                }
                response
            } catch (e: Exception) {
                // Log any exceptions that occur during the request
                Log.e("OpenAIRepository", "Exception occurred during API call: ${e.localizedMessage}")
                throw e // Rethrow the exception to handle it elsewhere if needed
            }
        }
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
