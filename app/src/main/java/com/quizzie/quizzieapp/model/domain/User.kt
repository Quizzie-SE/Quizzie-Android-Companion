package com.quizzie.quizzieapp.model.domain

const val CURRENT_USER_ID = 0

data class User(
    var userType: String?,
    var userId: String?,
    var name: String?,
    var email: String?,
    var mobileNumber: String?
)