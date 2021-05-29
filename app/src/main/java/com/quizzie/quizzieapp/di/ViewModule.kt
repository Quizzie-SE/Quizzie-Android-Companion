package com.quizzie.quizzieapp.di

import com.quizzie.quizzieapp.service.QuestionsParser
import com.quizzie.quizzieapp.service.QuestionsParserImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
interface ViewModule {

    @Binds
    fun bindQuestionsParser(parser: QuestionsParserImpl): QuestionsParser
}