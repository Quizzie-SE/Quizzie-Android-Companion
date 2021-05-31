package com.quizzie.quizzieapp.repository

import androidx.lifecycle.LiveData
import com.quizzie.quizzieapp.network.Errors
import com.quizzie.quizzieapp.network.RepoResult
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.Error

interface AuthRepository {
    suspend fun adminLogin(email: String, password: String): RepoResult<Unit>

    sealed class AuthError: Errors() {
        object InvalidCred: AuthError()
        object EmailUnver: AuthError()
    }
}