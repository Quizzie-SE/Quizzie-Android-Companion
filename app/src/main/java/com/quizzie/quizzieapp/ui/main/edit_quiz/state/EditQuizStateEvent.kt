package com.quizzie.quizzieapp.ui.main.edit_quiz.state

import androidx.annotation.StringRes
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.ui.common.BaseStateEvent
import java.lang.Exception

sealed class EditQuizStateEvent(
    @StringRes val defaultError: Int = R.string.unknown_error
) : BaseStateEvent(defaultError) {

    data class AddQuiz(val quiz: Quiz) : EditQuizStateEvent()

}