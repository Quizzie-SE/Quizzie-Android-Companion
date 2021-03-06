package com.quizzie.quizzieapp.network

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T,
    processErr: Exception.() -> Errors = { Errors.UnknownError }
): RepoResult<T> {
    return withContext(dispatcher) {
        try {
            RepoResult.Success(call())
        } catch (e: Exception) {
            Timber.e(e)
            RepoResult.Error(
                when (e) {
                    is SocketTimeoutException -> Errors.NetworkError
                    else -> e.processErr()
                }
            )
        }
    }
}

sealed class RepoResult<T> {
    class Success<T>(val res: T) : RepoResult<T>()
    class Error<T>(val err: Errors) : RepoResult<T>()
}

open class Errors {
    object NetworkError : Errors()
    object UnknownError : Errors()
}

