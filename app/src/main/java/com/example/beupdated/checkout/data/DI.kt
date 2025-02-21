package com.example.beupdated.checkout.data

import com.example.beupdated.checkout.domain.CheckOutRepository
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
    fun provideRepository(firebaseFirestore: FirebaseFirestore): CheckOutRepository = RepositoryImpl(firebaseFirestore)
}