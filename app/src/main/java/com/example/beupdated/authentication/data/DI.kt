package com.example.beupdated.authentication.data

import com.example.beupdated.authentication.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {
    @Singleton
    @Provides
    fun provideRepository(firebaseAuth: FirebaseAuth): AuthRepository = RepositoryImpl(firebaseAuth)
}