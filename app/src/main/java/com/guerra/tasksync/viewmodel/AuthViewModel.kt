package com.guerra.tasksync.viewmodel

import android.util.Log
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            if (result.data != null) {
                try {
                    syncUser(result.data)
                    _signInState.update {
                        it.copy(isSignInSuccessful = true, signInErrorMessage = null)
                    }
                } catch (e: Exception) {
                    _signInState.update {
                        it.copy(
                            isSignInSuccessful = false,
                            signInErrorMessage = "Sync failed: ${e.message}"
                        )
                    }
                }
            } else {
                _signInState.update {
                    it.copy(isSignInSuccessful = false, signInErrorMessage = result.errorMessage)
                }
            }
        }
    }

    fun onSignInCancelled() {
        _signInState.update { currentState ->
            currentState.copy(
                isSignInSuccessful = false,
                signInErrorMessage = "Sign in cancelled"
            )
        }
    }

    fun resetState() {
        _signInState.update { SignInState() }
    }

    fun signUpWithEmail(email: String, password: String, fullName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                val initial = fullName.first().lowercase()
                val userData = user?.let {
                    UserData(
                        userId = it.uid,
                        fullName = fullName,
                        profilePictureUrl = "https://raw.githubusercontent.com/Ecosynergy/EcoSynergy-App/refs/heads/main/app/src/main/res/drawable/$initial.png",
                        email = it.email
                    )
                }
                if (userData != null) {
                    val syncResult = syncUser(userData)
                    if (syncResult.isSuccess) {
                        _signInState.update { currentState ->
                            currentState.copy(isSignInSuccessful = true, signInErrorMessage = null)
                        }
                    } else {
                        _signInState.update { currentState ->
                            currentState.copy(
                                isSignInSuccessful = false,
                                signInErrorMessage = "User sync failed: ${syncResult.exceptionOrNull()?.message}"
                            )
                        }
                    }
                }

                sendEmailVerification(user ?: return@launch)
                Log.d("AuthViewModel", "Sign up process complete")
            } catch (e: Exception) {
                _signInState.update { currentState ->
                    currentState.copy(isSignInSuccessful = false, signInErrorMessage = "Sign up failed: ${e.message}")
                }
                Log.e("AuthViewModel", "Sign up failed", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _signInState.update { currentState ->
                    currentState.copy(isSignInSuccessful = true, signInErrorMessage = null)
                }
                Log.d("AuthViewModel", "Sign in successful")
            } catch (e: Exception) {
                _signInState.update { currentState ->
                    currentState.copy(
                        isSignInSuccessful = false,
                        signInErrorMessage = "Sign in failed: ${e.message}"
                    )
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

    fun sendEmailVerification(user: FirebaseUser){
        viewModelScope.launch {
            val result = userRepository.sendEmailVerification(user)
            if (result.isSuccess) {
                Log.d("AuthViewModel", "Email verification sent successfully")
            }
            else{
                Log.e("AuthViewModel", "Email verification gone wrong", result.exceptionOrNull())
            }
        }
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            val result = userRepository.sendPasswordResetEmail(email = email)
            if (result.isSuccess) {
                Log.d("AuthViewModel", "Password reset email sent successfully")
                onResult(true)
            }
            else{
                Log.e("AuthViewModel", "Password reset email gone wrong", result.exceptionOrNull())
                onResult(false)
            }
        }
    }

    fun deleteUser(user: FirebaseUser, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.deleteUser(user)
            if (result.isSuccess) {
                Log.d("AuthViewModel", "User deletion successful")
                onResult(true)
            } else {
                Log.e("AuthViewModel", "User deletion failed", result.exceptionOrNull())
                onResult(false)
            }
        }
    }

}