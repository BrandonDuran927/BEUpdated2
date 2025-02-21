package com.example.beupdated.registration.data

import android.content.Context
import androidx.room.Room
import com.example.beupdated.registration.data.local.EmailDAO
import com.example.beupdated.registration.data.local.PhoneNumberDAO
import com.example.beupdated.registration.data.local.PhoneNumberDatabase
import com.example.beupdated.registration.domain.Repository
import com.example.beupdated.registration.domain.TwilioApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Properties
import javax.inject.Singleton
import javax.mail.PasswordAuthentication
import javax.mail.Session

@Module
@InstallIn(SingletonComponent::class)
object DI {
    private const val BASE_URL = "https://api.twilio.com/2010-04-01/"

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): PhoneNumberDatabase =
        Room.databaseBuilder(
            context = context,
            klass = PhoneNumberDatabase::class.java,
            name = "PhoneNumberDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideImageDAO(db: PhoneNumberDatabase): PhoneNumberDAO = db.phoneNumberDAO

    @Singleton
    @Provides
    fun provideEmailDao(db: PhoneNumberDatabase): EmailDAO = db.emailDAO

    @Provides
    @Singleton
    fun provideRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase,
        twilioApi: TwilioApi,
        session: Session
    ): Repository = RepositoryImpl(firebaseAuth, database, twilioApi, session)

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://testttttttttttttttt22-default-rtdb.asia-southeast1.firebasedatabase.app")

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTwilioApi(retrofit: Retrofit): TwilioApi = retrofit.create(TwilioApi::class.java)

    @Provides
    @Singleton
    fun provideSession(): Session {
        val username = "duranbrandon927@gmail.com"
        val password = "pkep wgic tzpg oows"
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        return Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })
    }


}