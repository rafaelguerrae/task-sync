package com.guerra.tasksync.screen

import android.app.Activity.RESULT_OK
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.guerra.tasksync.screen.auth.InitialScreen
import com.guerra.tasksync.screen.auth.ResetPasswordScreen
import com.guerra.tasksync.screen.auth.SignInScreen
import com.guerra.tasksync.screen.auth.SignUpScreen
import com.guerra.tasksync.screen.main.MainScreen
import com.guerra.tasksync.viewmodel.AuthViewModel
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: AuthViewModel,
    context: Context,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.signInState.collectAsStateWithLifecycle()

    NavHost(navController = navController, startDestination = startDestination) {

        navigation(
            startDestination = Screen.InitialScreen.route,
            route = "auth"
        ) {

            composable(Screen.InitialScreen.route) {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            coroutineScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        } else {
                            viewModel.onSignInCancelled()
                        }
                    }
                )

                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        viewModel.resetState()
                        navController.navigate("main") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                InitialScreen(
                    state = state,
                    viewModel = viewModel,
                    onGoogleClick = {
                        coroutineScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    onSignInClick = {
                        navController.navigate(Screen.SignInScreen.route)
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.SignUpScreen.route)
                    })
            }

            composable(Screen.SignUpScreen.route) {
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        viewModel.resetState()
                        navController.navigate("main") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                SignUpScreen(
                    onFinish = { navController.popBackStack() },
                    onSignUpClick = { email, password, fullName ->
                        viewModel.signUpWithEmail(email, password, fullName)
                    },
                    viewModel = viewModel
                )
            }

            composable(Screen.SignInScreen.route) {
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        viewModel.resetState()
                        navController.navigate("main") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                SignInScreen(
                    viewModel = viewModel,
                    onFinish = { navController.popBackStack() },
                    onSignInClick = { email, password ->
                        viewModel.signInWithEmail(email, password)
                    },
                    onResetClick = {
                        navController.navigate(Screen.ResetPasswordScreen.route)
                    }
                )
            }

            composable(Screen.ResetPasswordScreen.route){

                ResetPasswordScreen(
                    onFinish = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }

        navigation(
            startDestination = Screen.MainScreen.route,
            route = "main"
        ) {
            composable(Screen.MainScreen.route) {
                MainScreen(
                    context = context,
                    navController = navController,
                    googleAuthUiClient = googleAuthUiClient,
                    coroutineScope = coroutineScope,
                    viewModel = viewModel
                )
            }
        }
    }

}