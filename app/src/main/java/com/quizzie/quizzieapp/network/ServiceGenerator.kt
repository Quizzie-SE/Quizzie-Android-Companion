package com.quizzie.quizzieapp.network

import com.quizzie.quizzieapp.util.BACKEND_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    private val baseClient: Retrofit
    private val backendClient: Retrofit
    fun <S> createService(serviceClass: Class<S>?): S {
        return baseClient.create(serviceClass)
    }

    fun <S> createTokenizedService(serviceClass: Class<S>?): S {
        return backendClient.create(serviceClass)
    }

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ContentTypeInterceptor())

        baseClient = retrofitBuilder.client(okHttpClientBuilder.build()).build()
        okHttpClientBuilder.addInterceptor(TokenInterceptor()).authenticator(TokenAuthenticator())
        backendClient = retrofitBuilder.client(okHttpClientBuilder.build()).build()
    }
}