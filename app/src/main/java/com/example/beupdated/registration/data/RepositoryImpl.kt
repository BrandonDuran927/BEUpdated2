package com.example.beupdated.registration.data

import android.util.Base64
import com.example.beupdated.common.utilities.CustomResult
import com.example.beupdated.registration.domain.Repository
import com.example.beupdated.registration.domain.TwilioApi
import com.example.beupdated.registration.domain.TwilioResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val session: Session
) : Repository {

    override suspend fun sendSmsOtp(
        phoneNumber: String,
        otp: String
    ): CustomResult<Unit> {
        val accountSid = "SECRET"
        val authToken = "SECRET"
        val twilioNumber = "+SECRET"
        val credentials =
            Base64.encodeToString("$accountSid:$authToken".toByteArray(), Base64.NO_WRAP)

        val body = "Your OTP is: $otp"
        val phNumber = "+631234566"

        return try {
            api.sendSms(
                accountSid = accountSid,
                authHeader = "Basic $credentials",
                to = phNumber,
                from = twilioNumber,
                body = body
            ).enqueue(object : retrofit2.Callback<TwilioResponse> {
                override fun onResponse(
                    call: Call<TwilioResponse>,
                    response: Response<TwilioResponse>
                ) {
                    if (response.isSuccessful) {
                        println(body)
                        println(phoneNumber)
                        println("SMS sent successfully: ${response.body()}")
                    } else {
                        println(body)
                        println(phoneNumber)
                        println("Failed to send SMS: ${response.errorBody()?.toString()}")
                    }
                }

                override fun onFailure(call: Call<TwilioResponse>, t: Throwable) {
                    println("Failed to send SMS: ${t.message}")
                }
            })
            CustomResult.Success(Unit)
        } catch (e: Exception) {
            CustomResult.Failure(e)
        }
    }

    override suspend fun sendEmailOtp(recipientEmail: String, otp: String): CustomResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val subject = "Your OTP Code"
                val body = "Your OTP code is: $otp"
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("duranbrandon927@gmail.com"))
                    setRecipients(
                        MimeMessage.RecipientType.TO,
                        InternetAddress.parse(recipientEmail)
                    )
                    setSubject(subject)
                    setText(body)
                }

                Transport.send(message)
                println("Email OTP: $otp")
                CustomResult.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                CustomResult.Failure(e)
                throw e
            }
        }
    }

    override suspend fun createAccount(email: String, password: String): Flow<CustomResult<AuthResult>> {
        return flow<CustomResult<AuthResult>> {  // Explicitly specify the type parameter
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            auth.signOut()
            println("Account created successfully: ${auth.currentUser}")
            emit(CustomResult.Success(result))
        }.catch { e ->
            e.printStackTrace()
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun addUserToFirebase(
        uid: String?,
        firstName: String,
        middleName: String,
        lastName: String,
        studentId: String,
        email: String,
        phoneNumber: String
    ): CustomResult<Unit> {
        return try {
            println("addUserToFirebase TRIGGERED")
            val usersRef = database.getReference("users")

            if (uid != null) {
                usersRef.child(uid).setValue(
                    mapOf(
                        "id" to studentId,
                        "firstName" to firstName,
                        "middleName" to middleName,
                        "lastName" to lastName,
                        "email" to email,
                        "phoneNumber" to phoneNumber
                    )
                ).await()
                println("User added successfully")
            } else {
                CustomResult.Failure(Exception("UID is null"))
            }
            CustomResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error adding user: ${e.message}")
            CustomResult.Failure(e)
        }
    }

    // override fun retrieveUsersRefOneTime()

    override fun retrieveUsersRef(): Flow<CustomResult<Map<String, Map<String, Any>>>> {
        return callbackFlow {
                println("Retrieve user ref called back")
                val usersReference = database.getReference("users")
                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val rawUsersReference = snapshot.getValue<Map<String, Map<String, Any>>>()
                        if (rawUsersReference != null) {
                            trySend(CustomResult.Success(rawUsersReference))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(error.toException())
                    }
                }

                usersReference.addValueEventListener(listener)

                // Ensure awaitClose is the last operation in the callbackFlow block
                awaitClose {
                    usersReference.removeEventListener(listener)
                }
        }
    }

    override fun retrieveUsersRefOneTime(): Flow<CustomResult<Map<String, Map<String, Any>>>> {
        return callbackFlow {
            val usersReference = database.getReference("users")
            usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val rawUsersReference = snapshot.getValue<Map<String, Map<String, Any>>>()
                    if (rawUsersReference != null) {
                        trySend(CustomResult.Success(rawUsersReference))
                    } else {
                        trySend(CustomResult.Failure(Exception("No data found")))
                    }
                    channel.close()
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(CustomResult.Failure(error.toException()))
                    channel.close()
                }
            })

            awaitClose{ /* no-op */ }
        }
    }
}


