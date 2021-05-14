package com.quizzie.quizzieapp.ui.main.edit_quiz.state

import com.quizzie.quizzieapp.ui.common.BaseViewEffect

sealed class CreateQuizViewEffect: BaseViewEffect() {
    data class ShowSnackBar(val msg: String): CreateQuizViewEffect()

}