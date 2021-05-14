package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.CreateQuizViewEffect
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.CreateQuizViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.InsertQuesViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.OnFragment
import com.quizzie.quizzieapp.ui.main.quizzes.QuizzesFragmentDirections
import com.quizzie.quizzieapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    val application: Application
) : ViewModel() {

    val createQuizViewState = CreateQuizViewState(application)
    val insertQuesViewState = InsertQuesViewState()
    val viewEffect = SingleLiveEvent<BaseViewEffect>()
    val isSaving = MutableLiveData(false)
    val onFragment = MutableLiveData(OnFragment.CREATE_QUIZ)

    init {

    }

    fun selectOption(index: Int, shouldRun: Boolean = true) {
        if (shouldRun && index in 0..3) {
            insertQuesViewState.correctOption.value = index
        }
    }

    fun createNewQuestion() = _selectQuestion(null)
    fun selectQuestion(question: Question) = _selectQuestion(question)
    fun selectQuestion(index: Int) = _selectQuestion(createQuizViewState.questions.value?.get(index))

    fun captureQuestion() {
        viewEffect.setValue(BaseViewEffect.NavigateTo(
            CreateQuizFragmentDirections.actionGlobalCameraFragment()))
    }

    fun notifyOnFragment(onFragment: OnFragment) {
        this.onFragment.value = onFragment
    }

    fun removeQuestion(index: Int) {
        createQuizViewState.questions.apply {
            value = value?.toMutableList()?.apply { removeAt(index) }
        }
    }

    fun save() {
        isSaving.value = true
        viewEffect.setValue(BaseViewEffect.NavigationPop)
    }

    private fun _selectQuestion(question: Question?) {
        insertQuesViewState.ques = question
        viewEffect.setValue(BaseViewEffect.NavigateTo(
            CreateQuizFragmentDirections.actionCreateQuizFragmentToInsertQuesFragment()))
    }

}