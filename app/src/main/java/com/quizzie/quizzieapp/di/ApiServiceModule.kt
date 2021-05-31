package com.quizzie.quizzieapp.di

import com.google.gson.Gson
import com.quizzie.quizzieapp.network.*
import com.quizzie.quizzieapp.util.BACKEND_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(
        gson: Gson,
        loggingInterceptor: HttpLoggingInterceptor,
        contentTypeInterceptor: ContentTypeInterceptor
    ): AuthService {
        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(contentTypeInterceptor)

        return retrofitBuilder.client(okHttpClientBuilder.build())
            .build()
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideBackendService(
        gson: Gson,
        loggingInterceptor: HttpLoggingInterceptor,
        contentTypeInterceptor: ContentTypeInterceptor,
        tokenInterceptor: TokenInterceptor
    ): BackendService {
        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(contentTypeInterceptor)
            .addInterceptor(tokenInterceptor)

        return retrofitBuilder.client(okHttpClientBuilder.build())
            .build()
            .create(BackendService::class.java)
    }

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level =  HttpLoggingInterceptor.Level.BODY
    }

}