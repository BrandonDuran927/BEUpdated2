package com.example.beupdated.registration.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneNumberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String = "",
    val phoneNumberInputTimeDate: String = "",
    val frequency: Int = 1
)

@Entity
data class EmailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String = "",
    val emailInputTimeDate: String = "",
    val frequency: Int = 1
)
