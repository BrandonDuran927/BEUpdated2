package com.example.beupdated.product.data

import com.example.beupdated.authentication.data.RepositoryImpl
import com.example.beupdated.product.domain.ProductRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideRepository(firebaseFirestore: FirebaseFirestore): ProductRepository = RepositoryImpl(firebaseFirestore)
}