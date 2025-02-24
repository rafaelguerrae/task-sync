package com.guerra.tasksync

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInErrorMessage: String? = null
)