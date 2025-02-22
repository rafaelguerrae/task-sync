package com.guerra.tasksync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.guerra.tasksync.ui.theme.TaskSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskSyncTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HelloWorld()
                }
            }
        }
    }
}

@Composable
fun HelloWorld(){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Hello World from TaskSync!")
    }
}