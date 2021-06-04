package com.quizzie.quizzieapp.model.data

import android.accounts.AuthenticatorDescription

data class QuestionResponse(
    var _id: String,
    var quizId: String,
    val description: String,
    val options: List<OptionResponse>,
    val correctAnswer: String
) {
    fun getUpdateResponse() = UpdateOps(
        listOf(
            UpdateResponse("description", description),
            UpdateResponse("options", options),
            UpdateResponse("correctAnswer", correctAnswer),
        )
    )
}

data class OptionResponse(val text: String)