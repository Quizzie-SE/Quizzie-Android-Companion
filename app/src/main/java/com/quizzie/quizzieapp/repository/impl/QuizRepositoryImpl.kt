package com.quizzie.quizzieapp.repository.impl


import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.repository.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class QuizRepositoryImpl @Inject constructor(

): QuizRepository {
    override suspend fun createQuiz(quiz: Quiz) {
        TODO("Not yet implemented")
    }

    override suspend fun addQuestion(quiz: Quiz) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllQuizzes(): List<Quiz> {
        TODO("Not yet implemented")
    }
}