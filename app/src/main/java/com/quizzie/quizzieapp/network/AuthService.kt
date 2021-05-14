package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.model.domain.Credentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/admin/login")
    suspend fun login(@Body credentials: Credentials)
}