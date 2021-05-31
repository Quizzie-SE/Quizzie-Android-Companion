package com.quizzie.quizzieapp.ui.main.edit_quiz.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Mode : Parcelable {
    CREATE, EDIT
}

enum class OnFragment {
    CREATE_QUIZ,
    ADD_QUES
}