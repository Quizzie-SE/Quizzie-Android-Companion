package com.quizzie.quizzieapp.repository.impl

import android.text.format.DateUtils

import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.repository.QuizRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepositoryMockImpl @Inject constructor(

): QuizRepository {
    override suspend fun createQuiz(quiz: Quiz) {
        TODO("Not yet implemented")
    }

    override suspend fun addQuestion(quiz: Quiz) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllQuizzes() = listOf(
            Quiz("Chem", 1620990300, 20, listOf(
                Question("What is Android?", listOf("OS", "A type of cake", "Phone", "None of the Above"), 0)
            ))
        )
}