package com.quizzie.quizzieapp.ui.main.edit_quiz.state

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.util.InputState
import com.quizzie.quizzieapp.util.addSource

class InsertQuesViewState {
    var ques: Question?
    get() = Question(
        question.input.value ?: "",
        options.map { it.value ?: "" },
        correctOption.value ?: 0
    )
    set(value){
        question.input.value = value?.question ?: ""
        correctOption.value = value?.correctOption ?: 0
        options.forEachIndexed { index, data -> data.value = value?.options?.get(index) ?: "" }
        mode.value = if (value == null) Mode.CREATE else Mode.EDIT
    }

    val question = InputState("", InputState.TEXT_REQUIRED)
    val correctOption = MutableLiveData(0)
    val options = MutableList<MutableLiveData<String>>(4) { MutableLiveData("") }
    val mode = MutableLiveData(Mode.CREATE)

    val hasError = MediatorLiveData<Boolean>()

    init {
        hasError.addSource(question.errorEnabled)
    }

}