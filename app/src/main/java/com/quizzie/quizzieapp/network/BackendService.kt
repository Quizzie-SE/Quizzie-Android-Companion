package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.model.domain.Credentials
import retrofit2.http.*

interface BackendService {

    @GET("/admin/created")
    suspend fun getAllQuizzes()

    @POST("/quiz/createQuiz")
    suspend fun createQuiz()

    @PATCH("/quiz/updateDetails/{quizId}")
    suspend fun updateQuiz(@Path("quizId") quizId: String)

    @DELETE("/quiz/delete")
    suspend fun deleteQuiz(@Body quizId: String)

    @GET("/question/all/{quizId}")
    suspend fun getAllQuestions(@Path("quizId") quizId: String)

    @POST("/question/add")
    suspend fun addQuestion()

    @PATCH("/question/update/{questionId}")
    suspend fun updateQuestion(@Path("questionId") questionId: String)

    @DELETE("/question/{questionId}")
    suspend fun deleteQuestion(@Path("questionId") questionId: String)

}