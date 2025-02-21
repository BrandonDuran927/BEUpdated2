package com.example.beupdated.profile.data

import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.profile.domain.Profile
import com.example.beupdated.profile.domain.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : ProfileRepository {
    override suspend fun fetchUserInfo(userId: String): CustomResult<Profile> {
        return try {
            val snapshot = firebaseDatabase
                .reference
                .child("users")
                .child(userId)
                .get()
                .await()

            if (!snapshot.exists()) {
                return CustomResult.Failure(Exception("User not found"))
            }

            val profile = snapshot.getValue(Profile::class.java)

            profile?.let {
                CustomResult.Success(it)
            } ?: CustomResult.Failure(Exception("Failed to parse user data"))
        } catch (e: Exception) {
            e.printStackTrace()
            CustomResult.Failure(e)
        }
    }

    override suspend fun deleteAccount(userId: String): Flow<CustomResult<Unit>> = flow {
        val user = firebaseAuth.currentUser
        if (user == null) {
            emit(CustomResult.Failure(Exception("User not logged in")))
            return@flow
        }

        try {
            // Delete the user from Firebase Authentication
            user.delete().await()

            // Delete the user document from Firebase Realtime Database
            firebaseDatabase.reference.child("users").child(userId).removeValue().await()

            emit(CustomResult.Success(Unit))
        } catch (e: Exception) {
            emit(CustomResult.Failure(e))
        }
    }.catch { e ->
        emit(CustomResult.Failure(e))
    }.flowOn(Dispatchers.IO) // Run the flow on the IO dispatcher


}