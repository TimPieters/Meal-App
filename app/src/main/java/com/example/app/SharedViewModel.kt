package com.example.app

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.Server.OpenAIRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class SharedViewModel : ViewModel() {
    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri
    private val openAIRepository = OpenAIRepository()
    fun setImageUri(uri: Uri?) {
        Log.d("SharedViewModel", "Image URI set: $uri")
        _capturedImageUri.value = uri
    }
    fun submitImageToOpenAI(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val imageStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes = imageStream?.readBytes()
            imageStream?.close()
            val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

            // Use your repository to send the encoded image to OpenAI's API
            // and handle the response (this is a simplification)
            val response = openAIRepository.analyzeImage(base64Image)
            // Process the response here...
        }
    }

}
