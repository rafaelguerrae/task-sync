package com.guerra.tasksync.viewmodel

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.guerra.tasksync.data.SignInResult
import com.guerra.tasksync.data.SignInState
import com.guerra.tasksync.data.UserData
import com.guerra.tasksync.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _state.update{it.copy(
            isSignInSuccessful = result.data != null,
            signInErrorMessage = result.errorMessage
        )}
        result.data?.let { user ->
            viewModelScope.launch {
                syncUser(user)
            }
        }
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

    fun signUpWithEmail(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                val userData = user?.let {
                    UserData(
                        userId = it.uid,
                        fullName = fullName,
                        profilePictureUrl = it.photoUrl?.toString(),
                        email = it.email
                    )
                }
                if (userData != null) {
                    syncUser(userData)
                }
            } catch (e: Exception) {
                // Update state with error
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                val userData = user?.let {
                    UserData(
                        userId = it.uid,
                        fullName = it.displayName,
                        profilePictureUrl = it.photoUrl?.toString(),
                        email = it.email
                    )
                }
            } catch (e: Exception) {
                // Update state with error
            }
        }
    }

    private suspend fun syncUser(userData: UserData): Result<Unit> {
        val userDoc = firestore.collection("users").document(userData.userId).get().await()
        return if (userDoc.exists()) {
            userRepository.updateUser(userData)
        } else {
            userRepository.createUser(userData)
        }
    }
}