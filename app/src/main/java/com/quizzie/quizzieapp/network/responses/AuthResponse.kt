package com.quizzie.quizzieapp.network.responses

import com.quizzie.quizzieapp.model.domain.User

data class AuthResponse(
    val message: String?,
    val userDetails: User?,
    var token: String?
)