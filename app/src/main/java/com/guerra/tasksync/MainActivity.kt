package com.guerra.tasksync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.guerra.tasksync.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

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
                    val viewModel: AuthViewModel by viewModels()

                    NavigationGraph(
                        navController = navController,
                        viewModel = viewModel,
                        context = applicationContext,
                        startDestination = if (googleAuthUiClient.getSignedInUser() != null) "main" else "auth",
                        googleAuthUiClient = googleAuthUiClient)
                }
            }
        }
    }
}