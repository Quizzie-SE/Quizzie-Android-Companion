package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.repository.pref.SessionManager
import com.quizzie.quizzieapp.util.BACKEND_AUTH_TOKEN_TYPE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {
    private lateinit var sessionManager: SessionManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original
            .newBuilder()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .header(
                "Authorization",
                "$BACKEND_AUTH_TOKEN_TYPE ${sessionManager.authToken?.accessToken}"
            )
            .method(original.method, original.body)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}