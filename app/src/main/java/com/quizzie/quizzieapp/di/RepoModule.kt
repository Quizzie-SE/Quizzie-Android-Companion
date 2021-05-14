package com.quizzie.quizzieapp.di

import com.quizzie.quizzieapp.repository.AuthRepository
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.repository.impl.AuthRepositoryImpl
import com.quizzie.quizzieapp.repository.impl.QuizRepositoryImpl
import com.quizzie.quizzieapp.repository.impl.QuizRepositoryMockImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Singleton
    @Binds
    fun bindAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    @Mock
    fun bindMockQuizRepository(quizRepository: QuizRepositoryMockImpl): QuizRepository

    @Singleton
    @Binds
    @Production
    fun bindQuizRepository(quizRepository: QuizRepositoryImpl): QuizRepository

}