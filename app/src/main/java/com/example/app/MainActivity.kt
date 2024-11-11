package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.app.Navigation.NavGraph
import com.example.app.ui.theme.AppTheme
import com.example.app.Server.OpenAIViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("OpenAITest", "MainActivity onCreate called")  // Confirm activity startup
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Toggle this to switch between the test button and your main app navigation
                    val isTesting = true

                    if (isTesting) {
                        OpenAITestContent()
                    } else {
                        val navController = rememberNavController()
                        val sharedViewModel: SharedViewModel by viewModels()
                        NavGraph(navController = navController, sharedViewModel = sharedViewModel)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OpenAITestContent(viewModel: OpenAIViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current  // For Toast messages
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Click the button to test OpenAI API connection")

        Button(
            onClick = {
                Log.d("OpenAITest", "Button clicked")
                println("Button clicked")  // This should appear in Logcat's standard output
                Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()  // Display a toast to confirm click

                val apiKey = "" // Replace with your actual OpenAI API key
                val prompt = "Hello, OpenAI!"

                viewModel.testConnection(apiKey, prompt) { response ->
                    Log.d("OpenAITest", "Response from API: $response")
                    println("OpenAI Response: $response")
                    Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Test Connection")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Tim")
    }
}