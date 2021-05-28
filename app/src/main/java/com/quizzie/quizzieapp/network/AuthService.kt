package com.quizzie.quizzieapp.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("/admin/login")
    fun adminLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    companion object {
        operator fun invoke(
        ): AuthService {

            return Retrofit.Builder()
                .baseUrl("http://quizzie-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }
    }
}