package com.guerra.tasksync.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            if (result.data != null) {
                try {
                    syncUser(result.data)
                    _state.update {
                        it.copy(isSignInSuccessful = true, signInErrorMessage = null)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(isSignInSuccessful = false, signInErrorMessage = "Sync failed: ${e.message}")
                    }
                }
            } else {
                _state.update {
                    it.copy(isSignInSuccessful = false, signInErrorMessage = result.errorMessage)
                }
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
        _loading.value = false
    }

    fun resetState() {
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
                onSignInCancelled()
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _state.update { currentState ->
                    currentState.copy(isSignInSuccessful = true, signInErrorMessage = null)
                }
                Log.d("AuthViewModel", "Sign in successful")
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(isSignInSuccessful = false, signInErrorMessage = "Sign in failed: ${e.message}")
                }
                Log.e("AuthViewModel", "Sign in failed", e)
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun getCurrentUserData(userId: String): UserData? {
        return try {
            val result = userRepository.getUser(userId)
            if (result.isSuccess) {
                result.getOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            UserData()
        }

    }

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            val data = getCurrentUserData(userId)
            _userData.value = data
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