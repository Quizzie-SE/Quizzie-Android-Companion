package com.quizzie.quizzieapp.repository

import com.quizzie.quizzieapp.model.data.QuestionResponse
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.network.RepoResult

interface QuizRepository {
    suspend fun createQuiz(quiz: Quiz): RepoResult<Quiz>

    suspend fun updateQuiz(quiz: Quiz): RepoResult<Unit>

    suspend fun getAllQuizzes(): RepoResult<List<Quiz>>

    suspend fun deleteQuiz(quiz: Quiz): RepoResult<Unit>
}