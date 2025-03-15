package com.guerra.tasksync.screen.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guerra.tasksync.R
import com.guerra.tasksync.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onFinish: () -> Unit,
    onSignUpClick: (String, String, String) -> Unit,
    viewModel: AuthViewModel
) {
    var step by remember { mutableIntStateOf(1) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showExitDialog by remember { mutableStateOf(false) }
    val maxSteps = 3

    val isLoading by viewModel.loading.collectAsStateWithLifecycle()

    val isPasswordCorrect = password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }

    val focusFullName = remember { FocusRequester() }
    val focusEmail = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {

            if (showExitDialog) {
                ExitDialog(
                    onDismiss = { showExitDialog = false },
                    onConfirm = {
                        onFinish()
                    }
                )
            }

            Text(
                text = stringResource(R.string.sign_up),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )

            IconButton(
                onClick = {
                    if (isLoading || step == 1) showExitDialog = true
                    else if (step > 1) step--
                    else onFinish()
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
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                    1 -> SignUpFullNameStep(
                        fullName = fullName,
                        onFullNameChange = { fullName = it },
                        onNext = {
                            step++
                        },
                        focusRequester = focusFullName
                    )

                    2 -> SignUpEmailStep(
                        email = email,
                        onEmailChange = { email = it },
                        onNext = {
                            step++
                        },
                        focusRequester = focusEmail
                    )

                    3 -> SignUpPasswordStep(
                        password = password,
                        onPasswordChange = { password = it },
                        isLoading = isLoading,
                        focusManager = focusManager,
                        onSignUpClick = {
                            if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Please fill in all fields.",
                                        withDismissAction = true
                                    )
                                }
                            } else if (!isPasswordCorrect) {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Ensure your password meets the requirements.",
                                        withDismissAction = true
                                    )
                                }
                            } else onSignUpClick(email, password, fullName)
                        },
                        focusRequester = focusPassword
                    )

                    else -> Text("Unknown step")
                }
            }


            Button(
                onClick = {
                    if (step < maxSteps) {
                        step++
                    } else {
                        if (email.isBlank() || password.isBlank() || fullName.isBlank()) {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Please fill in all fields.",
                                    withDismissAction = true
                                )
                            }
                        } else if (!isPasswordCorrect) {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Ensure your password meets the requirements.",
                                    withDismissAction = true
                                )
                            }
                        } else onSignUpClick(email, password, fullName)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                        text = if (step < maxSteps) {
                            stringResource(R.string.next_step)
                        } else if (!isLoading) {
                            stringResource(R.string.sign_up)
                        } else {
                            stringResource(R.string.signing_up)
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpFullNameStep(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    onNext: () -> Unit,
    focusRequester: FocusRequester
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.type_full_name)
        )

        Spacer(modifier = Modifier.size(16.dp))

        TextField(
            value = fullName,
            onValueChange = onFullNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text(stringResource(R.string.fullname)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                }
            )
        )
    }
}

@Composable
fun SignUpEmailStep(
    email: String,
    onEmailChange: (String) -> Unit,
    onNext: () -> Unit,
    focusRequester: FocusRequester
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text(stringResource(R.string.email)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNext()
                }
            )
        )
    }
}

@Composable
fun SignUpPasswordStep(
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    focusManager: FocusManager,
    onSignUpClick: () -> Unit,
    focusRequester: FocusRequester
) {
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.create_password)
        )

        Spacer(modifier = Modifier.size(16.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            enabled = !isLoading,
            placeholder = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignUpClick()
                }
            ),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            supportingText = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    PasswordPolicy(
                        stringResource(R.string.minimum_characters), password.length >= 8
                    )
                    PasswordPolicy(
                        stringResource(R.string.uppercase_characters),
                        password.any { it.isUpperCase() }
                    )
                    PasswordPolicy(
                        stringResource(R.string.lowercase_characters),
                        password.any { it.isLowerCase() }
                    )
                    PasswordPolicy(
                        stringResource(R.string.special_characters),
                        password.any { !it.isLetterOrDigit() }
                    )
                    PasswordPolicy(
                        stringResource(R.string.numeric_characters), password.any { it.isDigit() }
                    )
                }
            }
        )
    }
}

@Composable
fun PasswordPolicy(
    policy: String,
    isCheck: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = if (isCheck) Icons.Rounded.Check else Icons.Default.RadioButtonUnchecked,
            tint = if (isCheck) MaterialTheme.colorScheme.onSurface else Color.Gray,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = policy,
            fontSize = 12.sp,
            fontWeight = if (isCheck) FontWeight.Bold else FontWeight.Light,
            color = if (isCheck) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
    }
}

@Composable
fun ExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.exit)) },
        text = { Text(stringResource(R.string.your_sign_up_is_in_progress)) },
        confirmButton = {
            Button(onClick =
            {
                onConfirm()
                onDismiss()
            }
            ) {
                Text(stringResource(R.string.exit))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}