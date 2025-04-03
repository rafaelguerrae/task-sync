package com.guerra.tasksync

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.google.android.gms.tasks.Tasks
import com.guerra.tasksync.data.User
import com.guerra.tasksync.data.UserRepository
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertEquals

class UserRepositoryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var documentReference: DocumentReference

    @Before
    fun setup() {
        firestore = mock(FirebaseFirestore::class.java)
        firebaseAuth = mock(FirebaseAuth::class.java)
        documentReference = mock(DocumentReference::class.java)

        userRepository = UserRepository(firestore, firebaseAuth)
    }

    @Test
    fun `createUser should return success when firestore set is successful`() = runTest {
        // Arrange
        val testUser = User(
            userId = "test123",
            fullName = "John Doe",
            email = "john@example.com",
            profilePictureUrl = "http://example.com/pic.jpg"
        )

        // Setup the collection and document mocking
        val collectionMock = mock<com.google.firebase.firestore.CollectionReference> {
            whenever(it.document(testUser.userId)).thenReturn(documentReference)
        }
        whenever(firestore.collection("users")).thenReturn(collectionMock)

        // Mock the set task to be successful
        val setTask = Tasks.forResult<Void>(null)
        whenever(documentReference.set(testUser)).thenReturn(setTask)

        // Act
        val result = userRepository.createUser(testUser)

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateUser should return success when firestore update is successful`() = runTest {
        // Arrange
        val testUser = User(
            userId = "test123",
            fullName = "John Updated",
            email = "john@example.com",
            profilePictureUrl = "http://example.com/newpic.jpg"
        )

        // Setup the collection and document mocking
        val collectionMock = mock<com.google.firebase.firestore.CollectionReference> {
            whenever(it.document(testUser.userId)).thenReturn(documentReference)
        }
        whenever(firestore.collection("users")).thenReturn(collectionMock)

        // Mock the update task to be successful
        val updateTask = Tasks.forResult<Void>(null)
        whenever(documentReference.update(
            "fullName", testUser.fullName,
            "profilePictureUrl", testUser.profilePictureUrl,
            "email", testUser.email
        )).thenReturn(updateTask)

        // Act
        val result = userRepository.updateUser(testUser)

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getUser should return user when document exists`() = runTest {
        // Arrange
        val testUserId = "test123"
        val testUser = User(
            userId = testUserId,
            fullName = "John Doe",
            email = "john@example.com",
            profilePictureUrl = "http://example.com/pic.jpg"
        )

        // Setup the collection and document mocking
        val collectionMock = mock<com.google.firebase.firestore.CollectionReference> {
            whenever(it.document(testUserId)).thenReturn(documentReference)
        }
        whenever(firestore.collection("users")).thenReturn(collectionMock)

        // Mock the document snapshot
        val documentSnapshot = mock<DocumentSnapshot> {
            whenever(it.toObject(User::class.java)).thenReturn(testUser)
            whenever(it.exists()).thenReturn(true)
        }

        // Mock the get task to be successful
        val getTask = Tasks.forResult(documentSnapshot)
        whenever(documentReference.get()).thenReturn(getTask)

        // Act
        val result = userRepository.getUser(testUserId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
    }

    @Test
    fun `getUser should return failure when document not found`() = runTest {
        // Arrange
        val testUserId = "test123"

        // Setup the collection and document mocking
        val collectionMock = mock<com.google.firebase.firestore.CollectionReference> {
            whenever(it.document(testUserId)).thenReturn(documentReference)
        }
        whenever(firestore.collection("users")).thenReturn(collectionMock)

        // Mock the document snapshot
        val documentSnapshot = mock<DocumentSnapshot> {
            whenever(it.toObject(User::class.java)).thenReturn(null)
            whenever(it.exists()).thenReturn(false)
        }

        // Mock the get task to be successful
        val getTask = Tasks.forResult(documentSnapshot)
        whenever(documentReference.get()).thenReturn(getTask)

        // Act
        val result = userRepository.getUser(testUserId)

        // Assert
        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull()?.message == "User not found")
    }

    @Test
    fun `sendPasswordResetEmail should return success when email sent successfully`() = runTest {
        // Arrange
        val testEmail = "john@example.com"

        // Mock the password reset task
        val passwordResetTask = Tasks.forResult<Void>(null)
        whenever(firebaseAuth.sendPasswordResetEmail(testEmail)).thenReturn(passwordResetTask)

        // Act
        val result = userRepository.sendPasswordResetEmail(testEmail)

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteUser should return success when user deleted successfully`() = runTest {
        // Arrange
        val firebaseUser = mock<FirebaseUser> {
            whenever(it.uid).thenReturn("test123")
        }

        // Setup the collection and document mocking
        val collectionMock = mock<com.google.firebase.firestore.CollectionReference> {
            whenever(it.document(firebaseUser.uid)).thenReturn(documentReference)
        }
        whenever(firestore.collection("users")).thenReturn(collectionMock)

        // Mock the delete tasks
        val firestoreDeleteTask = Tasks.forResult<Void>(null)
        val userDeleteTask = Tasks.forResult<Void>(null)
        whenever(documentReference.delete()).thenReturn(firestoreDeleteTask)
        whenever(firebaseUser.delete()).thenReturn(userDeleteTask)

        // Act
        val result = userRepository.deleteUser(firebaseUser)

        // Assert
        assertTrue(result.isSuccess)
    }
}