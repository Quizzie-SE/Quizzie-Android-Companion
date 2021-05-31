package com.quizzie.quizzieapp.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quizzie.quizzieapp.di.DispatcherIO
import com.quizzie.quizzieapp.model.domain.AuthToken
import com.quizzie.quizzieapp.network.AuthService
import com.quizzie.quizzieapp.network.Errors
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.network.safeApiCall
import com.quizzie.quizzieapp.repository.AuthRepository
import com.quizzie.quizzieapp.repository.pref.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.*
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun adminLogin(email: String, password: String): RepoResult<Unit> {
        return safeApiCall {
            authService.adminLogin(email, password).let {
                sessionManager.addToken(it.token)
                sessionManager.addUserDetails(it.userDetails)
            }
        }
    }

    private suspend fun <T> safeApiCall(call: suspend () -> T) = safeApiCall(dispatcher, call) {

        when (this) {
            is HttpException -> {
                when (this.code()) {
                    409 -> AuthRepository.AuthError.EmailUnver
                    401 -> AuthRepository.AuthError.InvalidCred
                    else -> Errors.UnknownError
                }
            }
            else -> Errors.UnknownError
        }
    }
}