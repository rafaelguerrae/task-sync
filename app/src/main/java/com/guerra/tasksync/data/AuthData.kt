package com.guerra.tasksync.data

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)

data class UserData(
    val userId: String,
    val fullName: String?,
    val profilePictureUrl: String? = null,
    val email: String? = null
)