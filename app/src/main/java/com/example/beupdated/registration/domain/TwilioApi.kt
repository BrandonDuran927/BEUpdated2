package com.example.beupdated.registration.domain

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TwilioApi {
    @FormUrlEncoded
    @POST("Accounts/{AccountSID}/Messages.json")
    fun sendSms(
        @Path("AccountSID") accountSid: String,
        @Header("Authorization") authHeader: String,
        @Field("To") to: String,
        @Field("From") from: String,
        @Field("Body") body: String
    ): Call<TwilioResponse>
}