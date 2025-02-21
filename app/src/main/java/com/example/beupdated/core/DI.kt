package com.example.beupdated.core

import android.content.Context
import com.example.beupdated.core.network.ConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {
    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = ConnectivityObserver(context)
}