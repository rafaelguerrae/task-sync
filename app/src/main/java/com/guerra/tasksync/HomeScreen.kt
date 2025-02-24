package com.guerra.tasksync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit
){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        if(userData?.profilePictureUrl != null){
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "My profile picture",
                modifier = Modifier.size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        if(userData?.username != null){
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        Button(onClick = onSignOut){
            Text(text = "Sign out")
        }
    }

}