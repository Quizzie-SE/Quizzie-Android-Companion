package com.quizzie.quizzieapp.network

import androidx.annotation.StringRes
import com.quizzie.quizzieapp.model.data.*
import com.quizzie.quizzieapp.model.domain.Credentials
import com.quizzie.quizzieapp.model.domain.Quiz
import okhttp3.internal.http.hasBody
import retrofit2.http.*

interface BackendService {

    @GET("/admin/created")
    suspend fun getAllQuizzes(): ListResult<Quiz>

    @GET("/quiz/{id}")
    suspend fun getQuiz(@Path("id") id: String): Result<Quiz>

    @POST("/quiz/createQuiz")
    suspend fun createQuiz(@Body quiz: Quiz): Result<Quiz>

    @PATCH("/quiz/updateDetails/{quizId}")
    suspend fun updateQuiz(
        @Path("quizId") quizId: String,
        @Body quiz: UpdateOps
    )

    @HTTP(method = "DELETE", path = "/quiz/delete", hasBody = true)
    suspend fun deleteQuiz(@Body quiz: HashMap<String, String>)

    @GET("/question/all/{quizId}")
    suspend fun getAllQuestions(@Path("quizId") quizId: String): ListResult<QuestionResponse>

    @POST("/question/add")
    suspend fun addQuestion(@Body question: QuestionResponse)

    @PATCH("/question/update/{questionId}")
    suspend fun updateQuestion(
        @Path("questionId") questionId: String,
        @Body questions: List<UpdateResponse<out Any>>
    )

    @DELETE("/question/{questionId}")
    suspend fun deleteQuestion(@Path("questionId") questionId: String)

}