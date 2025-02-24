package com.guerra.tasksync

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
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

    fun resetState(){
        _state.update { SignInState() }
    }
}