package com.example.beupdated.registration.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PhoneNumberDAO {
    @Query("SELECT * FROM PhoneNumberEntity WHERE phoneNumber = :phoneNumber")
    suspend fun getPhoneNumber(phoneNumber: String): PhoneNumberEntity?

    @Insert
    suspend fun insertPhoneNumber(phoneNumber: PhoneNumberEntity)

    @Update
    suspend fun updatePhoneNumber(phoneNumber: PhoneNumberEntity)

    @Delete
    suspend fun deletePhoneNumber(phoneNumber: PhoneNumberEntity)

    @Query("DELETE FROM PhoneNumberEntity")
    suspend fun truncateTable()
}

@Dao
interface EmailDAO {
    @Query("SELECT * FROM EmailEntity WHERE email = :email")
    suspend fun getEmail(email: String): EmailEntity?

    @Insert
    suspend fun insertEmail(email: EmailEntity)

    @Update
    suspend fun updateEmail(email: EmailEntity)

    @Delete
    suspend fun deleteEmail(email: EmailEntity)

    @Query("DELETE FROM EmailEntity")
    suspend fun truncateTable()
}

