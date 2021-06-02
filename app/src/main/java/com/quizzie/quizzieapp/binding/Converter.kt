package com.quizzie.quizzieapp.binding

import androidx.databinding.InverseMethod
import com.quizzie.quizzieapp.model.domain.QuizType

object Converter {
    @JvmStatic
    fun floatToInt(value: Float) = value.toInt()

    @InverseMethod("floatToInt")
    @JvmStatic
    fun intToFloat(value: Int) = value.toFloat()

    @JvmStatic
    fun boolToQuizType(value: Boolean) = if (value) QuizType.Private else QuizType.Public

    @InverseMethod("boolToQuizType")
    @JvmStatic
    fun quizTypeToBool(value: QuizType) = value == QuizType.Private

}