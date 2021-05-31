package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.model.data.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @FormUrlEncoded
    @POST("/admin/login")
    suspend fun adminLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse
}