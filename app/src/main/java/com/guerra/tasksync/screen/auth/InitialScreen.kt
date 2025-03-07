package com.guerra.tasksync.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.guerra.tasksync.R
import com.guerra.tasksync.data.SignInState
import com.guerra.tasksync.viewmodel.AuthViewModel

@Composable
fun InitialScreen(
    state: SignInState,
    onGoogleClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: AuthViewModel
) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.isSignInSuccessful, key2 = state.signInErrorMessage) {
        if (state.isSignInSuccessful || state.signInErrorMessage != null) {
            isLoading = false
            viewModel.resetState()
        }
    }

    val isDarkTheme = isSystemInDarkTheme()
    val logoResId = if (isDarkTheme) {
        R.drawable.tasksync_nobg_white
    } else {
        R.drawable.tasksync_nobg_black
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(logoResId),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    isLoading = true
                    onGoogleClick()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                            trackColor = colorResource(R.color.blue)
                        )
                    }

                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = if (isLoading) stringResource(R.string.signing_in) else stringResource(R.string.enter_using_google),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

            }

            Button(
                onClick = {
                    onSignInClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_in_with_my_credentials),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(R.string.dont_have_account),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = stringResource(R.string.sign_up),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colorResource(R.color.blue),
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    }
                )
            }
        }
    }
}