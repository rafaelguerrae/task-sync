package com.guerra.tasksync.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : IUserRepository {

    override suspend fun createUser(userData: User): Result<Unit> {
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

    override suspend fun updateUser(userData: User): Result<Unit> {
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

    override suspend fun getUser(userId: String): Result<User> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(user: FirebaseUser): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .delete()
                .await()

            user.delete().await()
                ?: throw Exception("No authenticated user to delete")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(
        email: String
    ): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Log.d("UserRepository", "Password reset email sent.")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepository", "Password reset failed: ${e.message}")
            Result.failure(e)
        }
    }
}