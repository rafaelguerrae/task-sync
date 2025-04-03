package com.guerra.tasksync

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.guerra.tasksync.data.SignInResult
import com.guerra.tasksync.data.User
import com.guerra.tasksync.data.UserRepository
import com.guerra.tasksync.viewmodel.AuthViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var usersCollection: CollectionReference
    private lateinit var userDocument: DocumentReference

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        userRepository = mock(UserRepository::class.java)
        auth = mock(FirebaseAuth::class.java)
        firestore = mock(FirebaseFirestore::class.java)


        usersCollection = mock(CollectionReference::class.java)
        userDocument = mock(DocumentReference::class.java)
        whenever(firestore.collection("users")).thenReturn(usersCollection)
        whenever(usersCollection.document(org.mockito.kotlin.any())).thenReturn(userDocument)

        viewModel = AuthViewModel(userRepository, auth, firestore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSignInResult with valid data updates state to success`() = runTest {
        val dummyUser = User("123", "John Doe", "http://example.com/johndoe.png", "john@example.com")
        val signInResult = SignInResult(data = dummyUser, errorMessage = null)

        val fakeDocumentSnapshot = mock(DocumentSnapshot::class.java).apply {
            whenever(exists()).thenReturn(true)
        }

        whenever(userDocument.get()).thenReturn(Tasks.forResult(fakeDocumentSnapshot))

        whenever(userRepository.updateUser(dummyUser))
            .thenReturn(Result.success(Unit))

        viewModel.onSignInResult(signInResult)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.signInState.value
        assertTrue(state.isSignInSuccessful)
        assertNull(state.signInErrorMessage)
    }


    @Test
    fun `onSignInResult with error sets error message`(): Unit = runTest {
        val signInResult = SignInResult(data = null, errorMessage = "Sign in failed")

        viewModel.onSignInResult(signInResult)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.signInState.value
        assertFalse(state.isSignInSuccessful)
        assertEquals("Sign in failed", state.signInErrorMessage)
    }

    @Test
    fun `onSignInCancelled sets error message correctly`() {
        // Act
        viewModel.onSignInCancelled()
        val state = viewModel.signInState.value
        assertFalse(state.isSignInSuccessful)
        assertEquals("Sign in cancelled", state.signInErrorMessage)
    }

    // Additional tests could include:
    // - Testing signUpWithEmail (using runTest and stubbing FirebaseAuth.createUserWithEmailAndPassword)
    // - Testing signInWithEmail
    // - Testing sendPasswordResetEmail by capturing the callback result
    // - Testing deleteUser and loadUserData

    // You might want to write separate test cases for each public method to cover all paths.
}