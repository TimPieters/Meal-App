package com.example.app

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _capturedImageUri = MutableLiveData<Uri?>()
    val capturedImageUri: LiveData<Uri?> = _capturedImageUri

    fun setImageUri(uri: Uri?) {
        _capturedImageUri.value = uri
    }
}
