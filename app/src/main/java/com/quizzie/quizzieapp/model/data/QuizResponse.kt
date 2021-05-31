package com.quizzie.quizzieapp.model.data

data class QuizResponse(
    val quizName: String,
    val scheduledFor: String,
    val quizDuration: String,
    val quizType: String
)