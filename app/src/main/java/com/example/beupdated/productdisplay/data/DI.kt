package com.example.beupdated.productdisplay.data

import com.example.beupdated.authentication.domain.AuthRepository
import com.example.beupdated.productdisplay.domain.ProductDisplayRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {
    @Singleton
    @Provides
    fun provideRepository(authRepository: AuthRepository, firebaseDatabase: FirebaseDatabase, firebaseFirestore: FirebaseFirestore): ProductDisplayRepository =
        RepositoryImpl(authRepository, firebaseDatabase, firebaseFirestore)

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}