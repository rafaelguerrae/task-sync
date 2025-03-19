package com.guerra.tasksync.data

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)

data class User(
    val userId: String = "",
    val fullName: String? = null,
    val profilePictureUrl: String? = null,
    val email: String? = null
)