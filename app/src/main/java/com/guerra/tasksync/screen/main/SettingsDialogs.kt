package com.guerra.tasksync.screen.main

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.guerra.tasksync.R
import java.util.Locale


@Composable
fun SignOutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.sign_out)) },
        text = { Text(text = stringResource(R.string.sign_out_message)) },
        confirmButton = {
            Button(enabled = !isLoading,
                onClick = {
                    onConfirm()
                    isLoading = true
                }
            ) {
                if (isLoading) CircularProgressIndicator(
                    modifier = Modifier.size(10.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                    trackColor = colorResource(R.color.blue)
                )
                else Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ), enabled = !isLoading
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.delete_my_data)) },
        text = { Text(text = stringResource(R.string.delete_message)) },
        confirmButton = {
            Button(enabled = !isLoading,
                onClick = {
                    onConfirm()
                    isLoading = true
                }
            ) {
                if (isLoading) CircularProgressIndicator(
                    modifier = Modifier.size(10.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                    trackColor = colorResource(R.color.blue)
                )
                else Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ), enabled = !isLoading
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun ResetPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDone: () -> Unit,
    isPasswordDone: Boolean
) {
    var isLoading by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.password_animation))

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { if (!isPasswordDone) Text(text = stringResource(R.string.reset_password)) },
        text = {
            if (!isPasswordDone) Text(text = stringResource(R.string.reset_message))
            else {    Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){

                LottieAnimation(
                    composition = composition,
                    speed = 0.7f,
                    modifier = Modifier.size(150.dp)
                )
            } }
        },
        confirmButton = {
            if (!isPasswordDone) {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        onConfirm()
                        isLoading = true
                    }
                ) {
                    if (isLoading) CircularProgressIndicator(
                        modifier = Modifier.size(10.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                        trackColor = colorResource(R.color.blue)
                    )
                    else Text("Ok")
                }
            } else {
                Button(
                    onClick = {
                        onDone()
                    }, colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(stringResource(R.string.done))
                }
            }
        },
        dismissButton = {
            if (!isPasswordDone) {
                Button(
                    onClick = onDismiss, colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    ), enabled = !isLoading
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    )
}


@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val english = stringResource(R.string.english)
    val portuguese = stringResource(R.string.portuguese)
    val spanish = stringResource(R.string.spanish)
    val languages = listOf(english, portuguese, spanish)

    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.select_language)) },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedLanguage = when (language) {
                                    english -> "en"
                                    portuguese -> "pt"
                                    else -> "es"
                                }
                            }
                    ) {
                        RadioButton(
                            selected = (selectedLanguage == when (language) {
                                english -> "en"
                                portuguese -> "pt"
                                else -> "es"
                            }),
                            onClick = {
                                selectedLanguage = when (language) {
                                    english -> "en"
                                    portuguese -> "pt"
                                    else -> "es"
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = language)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLanguageSelected(selectedLanguage)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

fun updateLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    } else {
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        if (context is Activity) {
            context.recreate()
        }
    }
}