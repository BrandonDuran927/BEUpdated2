package com.example.beupdated.paymentsuccess.data

import android.content.Context
import com.example.beupdated.paymentsuccess.domain.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {
    @Provides
    @Singleton
    fun providePaymentRepository(@ApplicationContext context: Context): PaymentRepository {
        return RepositoryImpl(context)
    }
}