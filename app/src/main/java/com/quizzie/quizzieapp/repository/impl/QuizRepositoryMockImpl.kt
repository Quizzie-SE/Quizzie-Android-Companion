package com.quizzie.quizzieapp.repository.impl

import android.text.format.DateUtils
import com.quizzie.quizzieapp.model.data.QuestionResponse

import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.repository.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryMockImpl @Inject constructor(

): QuizRepository {
    override suspend fun createQuiz(quiz: Quiz): RepoResult<Quiz> {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuiz(quiz: Quiz): RepoResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllQuizzes(): RepoResult<List<Quiz>> = RepoResult.Success(listOf(
            Quiz("agahah", "Chem", 1620990300, 20, listOf(
                Question("jhdjhdj", "What is Android?", listOf("OS", "A type of cake", "Phone", "None of the Above"), 0)
            ))
        ))

    override suspend fun deleteQuiz(quiz: Quiz): RepoResult<Unit> {
        TODO("Not yet implemented")
    }


}