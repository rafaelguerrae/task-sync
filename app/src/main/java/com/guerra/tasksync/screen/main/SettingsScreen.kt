package com.guerra.tasksync.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.SubcomposeAsyncImage
import com.guerra.tasksync.R
import com.guerra.tasksync.data.User
import com.guerra.tasksync.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    userData: User,
    onSignOut: () -> Unit,
    onDelete: () -> Unit,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current

    val languagePresented = stringResource(R.string.language_description)
    val isDarkTheme = isSystemInDarkTheme()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showResetPasswordDialog by remember { mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val resetPasswordOkMessage = stringResource(R.string.reset_password_ok)
    val networkError = stringResource(R.string.network_error)

    var isPasswordDone by remember { mutableStateOf(false) }

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

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = appLanguage,
            onThemeSelected = { selected ->
                showThemeDialog = false
            },
            onDismiss = {
                showThemeDialog = false
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

    if (showDeleteDialog) {
        DeleteDialog(
            onConfirm = onDelete,
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    if (showResetPasswordDialog) {
        ResetPasswordDialog(
            onConfirm = {
                viewModel.sendPasswordResetEmail(userData.email ?: "") {
                    if (it) {
                        isPasswordDone = true
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = "$resetPasswordOkMessage: ${userData.email}",
                                withDismissAction = true
                            )
                        }
                    } else {
                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(
                                message = networkError,
                                withDismissAction = true
                            )
                        }
                    }
                }

            },
            onDismiss = {
                showResetPasswordDialog = false
            },
            isPasswordDone = isPasswordDone,
            onDone = {
                showResetPasswordDialog = false
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                SubcomposeAsyncImage(
                    model = userData.profilePictureUrl ?: R.drawable.default_pfp,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(90.dp)
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
                Spacer(modifier = Modifier.width(24.dp))


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
                        text = if (userData.fullName != null) "${userData.email}" else stringResource(
                            R.string.no_email
                        ),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        minLines = 1
                    )

                    Spacer(Modifier.size(12.dp))



                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ){
                            Text(
                                text = stringResource(R.string.edit_profile),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.tertiary)

                SettingsItem(
                    action = {showThemeDialog = true},
                    icon = if(isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
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
                    action = { showResetPasswordDialog = true },
                    icon = Icons.Default.ChangeCircle,
                    title = stringResource(R.string.reset_password),
                    description = stringResource(R.string.reset_password_description)
                )

//            SettingsItem(
//                action = { showDeleteDialog = true },
//                icon = Icons.Default.DeleteForever,
//                title = stringResource(R.string.delete_my_data),
//                description = stringResource(R.string.delete_my_data_description)
//            )

                SettingsItem(
                    action = { showSignOutDialog = true },
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = stringResource(R.string.sign_out)
                )
            }
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
            .height(60.dp)
            .clickable { action() }
    ) {
        Spacer(modifier = Modifier.size(24.dp))

        Icon(
            modifier = Modifier
                .size(25.dp),
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
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
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}