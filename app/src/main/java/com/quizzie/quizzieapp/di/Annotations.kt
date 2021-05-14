package com.quizzie.quizzieapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Mock

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Production