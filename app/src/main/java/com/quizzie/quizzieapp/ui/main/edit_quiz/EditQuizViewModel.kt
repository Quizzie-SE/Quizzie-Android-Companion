package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar as FSnackbar
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.di.Mock
import com.quizzie.quizzieapp.di.Production
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.ui.common.Snackbar
import com.quizzie.quizzieapp.ui.common.StandardErrorHandler
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.CreateQuizViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.InsertQuesViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.Mode
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.OnFragment
import com.quizzie.quizzieapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    val application: Application,
    @Production private val repository: QuizRepository
) : ViewModel() {

    val createQuizViewState = CreateQuizViewState(application)
    val insertQuesViewState = InsertQuesViewState()
    val viewEffect = SingleLiveEvent<BaseViewEffect>()
    val isSaving = MutableLiveData(false)
    private var selectedIndex: Int? = null
    private val onFragment = MutableLiveData(OnFragment.CREATE_QUIZ)
    private var isViewModelInitialized = false
    private val errHandler = StandardErrorHandler(viewEffect, application)

    init {
        onFragment.observeForever {
            if (it == OnFragment.CREATE_QUIZ) {
                insertQuesViewState.ques = null
            }
        }
    }

    fun initializeQuiz(quiz: Quiz) {
        if (!isViewModelInitialized) {
            isViewModelInitialized = true
            createQuizViewState.quiz = quiz
        }
    }

    fun selectOption(index: Int, shouldRun: Boolean = true) {
        if (shouldRun && index in 0..3) {
            insertQuesViewState.correctOption.value = index
        }
    }

    fun createNewQuestion() = _selectQuestion(null)
    fun selectQuestion(question: Question) = _selectQuestion(question)
    fun selectQuestion(index: Int) =
        _selectQuestion(createQuizViewState.questions.value?.get(index))

    fun captureQuestion() {
        viewEffect.setValue(
            BaseViewEffect.NavigateTo(
                CreateQuizFragmentDirections.actionGlobalCameraFragment()
            )
        )
    }

    fun notifyOnFragment(onFragment: OnFragment) {
        this.onFragment.value = onFragment
    }

    fun removeQuestion(index: Int) {
        createQuizViewState.questions.editListLD {
            val removedQuestion = removeAt(index)
            viewEffect.setValue(
                BaseViewEffect.ShowSnackBar(
                    Snackbar(
                        application.getString(R.string.item_removed),
                        application.getString(R.string.undo), {
                            createQuizViewState.questions.editListLD { add(index, removedQuestion) }
                        }, FSnackbar.LENGTH_LONG
                    )
                )
            )
        }
    }

    fun save() {
        viewModelScope.launch {
            if (onFragment.value == OnFragment.ADD_QUES) {
                if (insertQuesViewState.hasError) {
                    viewEffect.setValue(BaseViewEffect.ShowSnackBar(Snackbar(application.getString(R.string.pls_fill_all_fields))))
                    return@launch
                }
                createQuizViewState.questions.editListLD {
                    insertQuesViewState.ques?.let { ques ->
                        selectedIndex?.also {
                            removeAt(it)
                            add(it, ques)
                        } ?: add(ques)
                    }
                }
                viewEffect.setValue(BaseViewEffect.NavigationPop)
            } else {
                if (createQuizViewState.hasError.value == true) return@launch
                val quiz = createQuizViewState.quiz!!
                isSaving.value = true
                val result = if (createQuizViewState.mode.value == Mode.CREATE) {
                    repository.createQuiz(quiz)
                } else {
                    repository.updateQuiz(quiz)
                }

                if (result is RepoResult.Error) {
                    errHandler.handle(result.err)
                } else {
                    viewEffect.setValue(
                        BaseViewEffect.SetFragmentResult(
                            EDIT_QUIZ_VALUE_KEY,
                            "RESULT" to createQuizViewState.mode.value,
                            "VALUE" to quiz
                        )
                    )
                    viewEffect.setValue(BaseViewEffect.NavigationPop)
                }
                isSaving.value = false
            }
        }
    }

    private fun _selectQuestion(question: Question?) {
        insertQuesViewState.ques = question
        selectedIndex = createQuizViewState.questions.value?.indexOf(question)
        if (selectedIndex == -1) selectedIndex = null
        viewEffect.setValue(
            BaseViewEffect.NavigateTo(
                CreateQuizFragmentDirections.actionCreateQuizFragmentToInsertQuesFragment()
            )
        )
    }
}