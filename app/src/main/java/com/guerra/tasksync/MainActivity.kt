package com.guerra.tasksync

import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.guerra.tasksync.screen.NavigationGraph
import com.guerra.tasksync.ui.theme.TaskSyncTheme
import com.guerra.tasksync.viewmodel.AuthViewModel
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import com.guerra.tasksync.viewmodel.TeamsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var loaded by mutableStateOf(false)

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languageCode = prefs.getString("language_code", "en") ?: "en"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
        }
        val localizedContext = newBase.createConfigurationContext(config)
        super.attachBaseContext(localizedContext)
    }


    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { !loaded }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val context = LocalContext.current
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val appTheme by remember { mutableStateOf(prefs.getString("theme_code", "device") ?: "device") }

            TaskSyncTheme(appTheme = appTheme){
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = if(appTheme == "dark") false else if(appTheme == "light") true else !isSystemInDarkTheme()
                val statusBarColor = MaterialTheme.colorScheme.surface

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = statusBarColor,
                        darkIcons = useDarkIcons
                    )
                }

                Surface(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel by viewModels()
                    val teamsViewModel: TeamsViewModel by viewModels()

                    NavigationGraph(
                        appTheme = appTheme,
                        navController = navController,
                        authViewModel = authViewModel,
                        teamsViewModel = teamsViewModel,
                        context = applicationContext,
                        startDestination = if (googleAuthUiClient.getSignedInUser() != null) "main" else "auth",
                        googleAuthUiClient = googleAuthUiClient,
                        onLoaded = { loaded = true }
                    )
                }
            }
        }
    }
}