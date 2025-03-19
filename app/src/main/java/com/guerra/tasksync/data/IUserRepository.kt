package com.guerra.tasksync.data

import com.google.firebase.auth.FirebaseUser

interface IUserRepository {
    suspend fun createUser(userData: User): Result<Unit>
    suspend fun updateUser(userData: User): Result<Unit>
    suspend fun getUser(userId: String): Result<User>
    suspend fun deleteUser(user: FirebaseUser): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}