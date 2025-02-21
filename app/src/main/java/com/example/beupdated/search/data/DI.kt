package com.example.beupdated.search.data

import com.example.beupdated.search.domain.SearchRepository
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideRepository(firebaseFirestore: FirebaseFirestore) : SearchRepository = RepositoryImpl(firebaseFirestore)
}