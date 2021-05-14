package com.quizzie.quizzieapp.repository

import com.quizzie.quizzieapp.model.domain.Quiz

interface QuizRepository {

    suspend fun createQuiz(quiz: Quiz)

    suspend fun addQuestion(quiz: Quiz)

    suspend fun getAllQuizzes(): List<Quiz>
}