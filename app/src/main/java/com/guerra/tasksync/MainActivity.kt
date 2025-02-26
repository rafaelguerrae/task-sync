package com.guerra.tasksync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.identity.Identity
import com.guerra.tasksync.screen.NavigationGraph
import com.guerra.tasksync.ui.theme.TaskSyncTheme
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import com.guerra.tasksync.viewmodel.SignInViewModel

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskSyncTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val statusBarColor = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = useDarkIcons
                    )
                }

                Surface(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
                    val navController = rememberNavController()
                    val startDestination = if (googleAuthUiClient.getSignedInUser() != null) "home" else "sign_in"
                    val viewModel = viewModel<SignInViewModel>()

                    NavigationGraph(
                        navController = navController,
                        viewModel = viewModel,
                        context = applicationContext,
                        startDestination = startDestination,
                        googleAuthUiClient = googleAuthUiClient)

                }
            }
        }
    }
}