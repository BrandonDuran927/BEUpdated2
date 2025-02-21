package com.example.beupdated.savedproduct.data

import com.example.beupdated.savedproduct.domain.SavedProductRepository
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideRepository(firebaseFirestore: FirebaseFirestore): SavedProductRepository = RepositoryImpl(firebaseFirestore)
}