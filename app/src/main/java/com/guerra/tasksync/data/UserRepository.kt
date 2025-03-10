package com.guerra.tasksync.data

interface UserRepository {
    suspend fun createUser(userData: UserData): Result<Unit>
    suspend fun updateUser(userData: UserData): Result<Unit>
    suspend fun getUser(userId: String): Result<UserData>
    suspend fun deleteUser(userData: UserData): Result<Unit>
}