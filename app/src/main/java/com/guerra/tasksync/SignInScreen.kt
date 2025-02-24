package com.guerra.tasksync

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
){
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInErrorMessage) {
        state.signInErrorMessage?.let{error->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = onSignInClick
        ) {
            Text(text = "Sign In")
        }
    }
}