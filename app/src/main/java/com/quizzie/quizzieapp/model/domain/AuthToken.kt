package com.quizzie.quizzieapp.model.domain

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
