package com.example.beupdated.registration.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhoneNumberEntity::class, EmailEntity::class],
    version = 3
)
abstract class PhoneNumberDatabase: RoomDatabase() {
    abstract val phoneNumberDAO: PhoneNumberDAO
    abstract val emailDAO: EmailDAO
}


