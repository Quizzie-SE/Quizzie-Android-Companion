package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.repository.pref.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ContentTypeInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original
            .newBuilder()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .method(original.method, original.body)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}