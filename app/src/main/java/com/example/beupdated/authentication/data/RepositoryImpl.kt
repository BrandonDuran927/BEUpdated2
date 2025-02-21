package com.example.beupdated.authentication.data

import android.util.Log
import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.common.utilities.CustomResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<CustomResult<AuthResult>> {
        return channelFlow {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                trySend(CustomResult.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(CustomResult.Failure(e))
            }
            awaitClose()
        }
    }

    override suspend fun signOutUser(): CustomResult<Unit> {
        return try {
            val result = firebaseAuth.signOut()
            CustomResult.Success(result)
        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }

    override suspend fun resetAccount(email: String): Flow<CustomResult<Void>> {
        return flow {
            try {
                val result = firebaseAuth.sendPasswordResetEmail(email).await()

                emit(CustomResult.Success(result))
            } catch (e: Exception) {
                emit(CustomResult.Failure(e))
            }
        }
    }

    override fun retrieveUser(): Flow<FirebaseUser?> {
         return flow {
             try {
                 val result = firebaseAuth.currentUser
                 emit(result)
             } catch (e: Exception) {
                 e.printStackTrace()
                 throw e
             }
         }
    }
}
