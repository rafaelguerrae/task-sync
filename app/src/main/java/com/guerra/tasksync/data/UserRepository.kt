package com.guerra.tasksync.data

import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun createUser(userData: UserData): Result<Unit>
    suspend fun updateUser(userData: UserData): Result<Unit>
    suspend fun getUser(userId: String): Result<UserData>
    suspend fun deleteUser(user: FirebaseUser): Result<Unit>

    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}