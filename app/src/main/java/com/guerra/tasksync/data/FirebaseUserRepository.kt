package com.guerra.tasksync.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun createUser(userData: UserData): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userData.userId)
                .set(userData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(userData: UserData): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userData.userId)
                .update(
                    "fullName", userData.fullName,
                    "profilePictureUrl", userData.profilePictureUrl,
                    "email", userData.email
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(userId: String): Result<UserData> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            val user = snapshot.toObject(UserData::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userData: UserData): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userData.userId)
                .delete()
                .await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}