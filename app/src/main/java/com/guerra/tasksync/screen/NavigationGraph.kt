package com.guerra.tasksync.screen

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.guerra.tasksync.viewmodel.GoogleAuthUiClient
import com.guerra.tasksync.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: SignInViewModel,
    context: Context,
    startDestination: String,
    googleAuthUiClient: GoogleAuthUiClient
) {
    val coroutineScope = rememberCoroutineScope()

    NavHost( navController = navController, startDestination = startDestination) {

        navigation(
            startDestination = Screen.InitialScreen.route,
            route = "auth"
        ){

            composable(Screen.InitialScreen.route) {
                val state by viewModel.state.collectAsStateWithLifecycle()

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
                        Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show()

                        viewModel.resetState()

                        navController.navigate("main") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                InitialScreen(state = state, viewModel = viewModel, onSignInClick = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                })
            }


            composable(Screen.SignUpScreen.route){}
            composable(Screen.SignInScreen.route){}


        }

        navigation(
            startDestination = Screen.MainScreen.route,
            route = "main"
        ){
            composable(Screen.MainScreen.route) {
                MainScreen(
                    context = context,
                    navController = navController,
                    googleAuthUiClient = googleAuthUiClient,
                    coroutineScope = coroutineScope
                )
            }



        }





    }

}