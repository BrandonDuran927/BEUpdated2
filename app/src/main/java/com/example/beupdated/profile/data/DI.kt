package com.example.beupdated.profile.data

import com.example.beupdated.profile.domain.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {
    @Provides
    @Singleton
    fun provideRepository(firebaseDatabase: FirebaseDatabase, firebaseAuth: FirebaseAuth) : ProfileRepository = RepositoryImpl(firebaseDatabase, firebaseAuth)
}