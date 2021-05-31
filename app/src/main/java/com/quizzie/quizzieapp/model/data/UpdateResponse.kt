package com.quizzie.quizzieapp.model.data

data class UpdateResponse<T> (
    val propName: String,
    val value: T
)

data class UpdateOps(
    val updateOps: List<UpdateResponse<Any>>
)