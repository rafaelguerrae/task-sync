package com.guerra.tasksync.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TeamsScreen(){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "This will be your teams screen", modifier = Modifier.align(Alignment.Center))
    }
}