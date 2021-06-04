package com.quizzie.quizzieapp.model.domain

import com.google.gson.annotations.SerializedName

enum class QuizType {
    @SerializedName(value = "Public", alternate = ["public"])
    Public,
    @SerializedName("Private", alternate = ["private"])
    Private
}
