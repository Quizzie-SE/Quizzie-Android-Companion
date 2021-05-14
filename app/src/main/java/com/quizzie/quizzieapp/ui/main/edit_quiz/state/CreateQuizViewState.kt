package com.quizzie.quizzieapp.ui.main.edit_quiz.state

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.util.*

class CreateQuizViewState(context: Context) {
    var quiz: Quiz?
    get() = Quiz(
        name.input.value ?: "",
        (date.input.value + time.input.value).toLongDate("$DATE_FORMAT $TIME_FORMAT") ?: 0,
        duration.input.value?.toLong() ?: 0,
        questions.value ?: listOf()
    )
    set(value) {
        name.input.value = value?.quizName ?: ""
        date.input.value = (value?.quizDate ?: now).toDate(DATE_FORMAT)
        time.input.value = (value?.quizDate ?: now).toDate(TIME_FORMAT)
        duration.input.value = value?.duration?.toInt() ?: 15
        questions.value = value?.questions ?: listOf()
        mode.value = if (value == null) Mode.CREATE else Mode.EDIT
    }

    val name = InputState("", InputState.TEXT_REQUIRED)
    val date = InputState("", InputState.TEXT_REQUIRED, InputState.BAD_DATE_FORMAT(DATE_FORMAT))
    val time = InputState("", InputState.TEXT_REQUIRED, InputState.BAD_DATE_FORMAT(TIME_FORMAT))
    val duration = InputState(0, InputState.ErrorCondition(
        context.getString(R.string.duration_greater_than_0)
    ) { input, _ -> input == 0 })
    val questions = MutableLiveData(listOf<Question>())
    val mode = MutableLiveData(Mode.CREATE)

    val hasError = MediatorLiveData<Boolean>()

    init {
        hasError.addSource(name.errorEnabled, date.errorEnabled, time.errorEnabled, duration.errorEnabled)
    }

}
