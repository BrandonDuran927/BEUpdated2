package com.example.beupdated.profile.domain

import com.example.beupdated.common.utilities.CustomResult
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun fetchUserInfo(userId: String) : CustomResult<Profile>
    suspend fun deleteAccount(userId: String) : Flow<CustomResult<Unit>>

}