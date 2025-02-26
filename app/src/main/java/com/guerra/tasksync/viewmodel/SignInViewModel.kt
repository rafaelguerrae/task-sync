package com.guerra.tasksync.viewmodel

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import com.guerra.tasksync.data.SignInResult
import com.guerra.tasksync.data.SignInState
import kotlinx.coroutines.flow.MutableStateFlow

class SignInViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update{it.copy(
            isSignInSuccessful = result.data != null,
            signInErrorMessage = result.errorMessage
        )}
    }

    fun onSignInCancelled() {
        _state.update { currentState ->
            currentState.copy(
                isSignInSuccessful = false,
                signInErrorMessage = "Sign in cancelled"
            )
        }
    }

    fun resetState(){
        _state.update { SignInState() }
    }
}