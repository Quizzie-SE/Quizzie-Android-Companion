package com.quizzie.quizzieapp.model.data

import androidx.annotation.StringRes

data class ListResult<T>(
    val message: String,
    val result: List<T>
)

data class Result<T>(
    val result: T
)