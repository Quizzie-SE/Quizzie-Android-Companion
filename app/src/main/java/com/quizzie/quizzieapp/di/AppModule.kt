package com.quizzie.quizzieapp.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Dispatcher
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @DispatcherIO
    @Provides
    fun provideIODispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideExternalScope() = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideGson() = Gson()

}