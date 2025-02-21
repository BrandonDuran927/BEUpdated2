package com.example.beupdated.order.data

import com.example.beupdated.order.domain.OrderRepository
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
    fun provideRepository(firebaseFirestore: FirebaseFirestore) : OrderRepository = RepositoryImpl(firebaseFirestore)
}