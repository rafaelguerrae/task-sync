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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.guerra.tasksync.R
import com.guerra.tasksync.data.UserData
import java.util.Locale

@Composable
fun SettingsScreen(
    userData: UserData,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current

    val languagePresented = stringResource(R.string.language_description)

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    var appLanguage by remember {
        mutableStateOf(
            when (languagePresented) {
                "English" -> "en"
                "Português" -> "pt"
                else -> "es"
            }
        )
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = appLanguage,
            onLanguageSelected = { selected ->
                appLanguage = selected
                updateLocale(context, appLanguage)
                showLanguageDialog = false
            },
            onDismiss = {
                showLanguageDialog = false
            }
        )
    }

    if (showSignOutDialog) {
        SignOutDialog(
            onConfirm = onSignOut,
            onDismiss = {
                showSignOutDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            SubcomposeAsyncImage(
                model = userData.profilePictureUrl ?: R.drawable.default_pfp,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                        trackColor = colorResource(R.color.blue)
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (userData.fullName != null) "${userData.fullName}" else stringResource(
                        R.string.no_fullname
                    ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    minLines = 1
                )

                Text(
                    text = if (userData.fullName != null) "${userData.email}" else stringResource(R.string.no_email),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    minLines = 1
                )

                Spacer(Modifier.size(4.dp))

                Row(
                    modifier = Modifier.clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Default.ModeEdit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.size(4.dp))

                    Text(
                        text = stringResource(R.string.edit_profile),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)

        SettingsItem(
            action = {},
            icon = Icons.Default.LightMode,
            title = stringResource(R.string.theme),
            description = stringResource(R.string.theme_description)
        )

        SettingsItem(
            action = {
                showLanguageDialog = true
            },
            icon = Icons.Default.Language,
            title = stringResource(R.string.language),
            description = stringResource(R.string.language_description)
        )

        SettingsItem(
            action = {},
            icon = Icons.Default.Info,
            title = stringResource(R.string.info),
            description = stringResource(R.string.info_description)
        )

        SettingsItem(
            action = {},
            icon = Icons.Default.DeleteForever,
            title = stringResource(R.string.delete_my_data),
            description = stringResource(R.string.delete_my_data_description)
        )

        SettingsItem(
            action = { showSignOutDialog = true },
            icon = Icons.AutoMirrored.Filled.Logout,
            title = stringResource(R.string.sign_out)
        )
    }
}
}


@Composable
fun SettingsItem(
    action: () -> Unit,
    icon: ImageVector,
    title: String,
    description: String = ""
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { action() }
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        Icon(
            modifier = Modifier
                .size(30.dp),
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp
            )

            if (description != "") {
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }

    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.tertiary)
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
            Button(
                onClick = {
                    onConfirm()
                    isLoading = true
                }
            ) {
                if(isLoading)  CircularProgressIndicator(
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
                )
            ) {
                Text(stringResource(R.string.cancel))
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