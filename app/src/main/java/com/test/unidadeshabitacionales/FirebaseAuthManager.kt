package com.test.unidadeshabitacionales

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager private constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Singleton instance
    companion object {
        val instance: FirebaseAuthManager by lazy { FirebaseAuthManager() }
    }

    // Sign Up
    suspend fun signUp(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Log In
    suspend fun logIn(email: String, password: String): Result<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Log Out
    fun logOut() {
        auth.signOut()
    }

    // Reset Password
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get Current User
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Update Email
    suspend fun updateEmail(newEmail: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("No current user"))
        return try {
            user.updateEmail(newEmail).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update Password
    suspend fun updatePassword(newPassword: String): Result<Unit> {
        val user = auth.currentUser ?: return Result.failure(Exception("No current user"))
        return try {
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
