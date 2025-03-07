package com.guerra.tasksync.screen.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guerra.tasksync.R


@Composable
fun SignInScreen(
    onFinish: () -> Unit,
    onSignInClick: () -> Unit
){
    var step by remember { mutableIntStateOf(1) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val maxSteps = 2
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        IconButton(
            onClick = {
                if (step > 1) {
                    step--
                } else {
                    onFinish()
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(35.dp)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (step) {
                1 -> SignInEmailStep(email = email, onEmailChange = { email = it })
                2 -> SignInPasswordStep(password = password, onPasswordChange = { password = it })
                else -> Text(stringResource(R.string.unknown_step))
            }
        }


        Button(
            onClick = {
                if (step < maxSteps) {
                    step++
                } else {
                    isLoading = true
                    onSignInClick()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ){
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
                    text = if (step < maxSteps) "Next" else if(!isLoading)stringResource(R.string.signing_in) else stringResource(R.string.signing_in),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SignInEmailStep(
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.type_email)
        )

        Spacer(modifier = Modifier.size(16.dp))

        TextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.email)) }
        )
    }
}

@Composable
fun SignInPasswordStep(
    password: String,
    onPasswordChange: (String) -> Unit

) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.type_password)
        )

        Spacer(modifier = Modifier.size(16.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )
    }
}
