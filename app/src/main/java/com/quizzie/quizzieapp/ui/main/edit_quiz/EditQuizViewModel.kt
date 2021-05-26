package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.di.Mock
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.CreateQuizViewEffect
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.CreateQuizViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.InsertQuesViewState
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.OnFragment
import com.quizzie.quizzieapp.ui.main.quizzes.QuizzesFragmentDirections
import com.quizzie.quizzieapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuizViewModel @Inject constructor(
    val application: Application,
    @Mock private val repository: QuizRepository
) : ViewModel() {

    val createQuizViewState = CreateQuizViewState(application)
    val insertQuesViewState = InsertQuesViewState()
    val viewEffect = SingleLiveEvent<BaseViewEffect>()
    val isSaving = MutableLiveData(false)
    val onFragment = MutableLiveData(OnFragment.CREATE_QUIZ)
    private var isViewModelInitialized = false

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
                        }, com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    )
                )
            )
        }
    }

    fun save() {
        if (onFragment.value == OnFragment.ADD_QUES) {
            createQuizViewState.questions.editListLD { insertQuesViewState.ques?.let { add(it) } }
        }
        viewEffect.setValue(BaseViewEffect.NavigationPop)
    }

    private fun _selectQuestion(question: Question?) {
        insertQuesViewState.ques = question
        viewEffect.setValue(
            BaseViewEffect.NavigateTo(
                CreateQuizFragmentDirections.actionCreateQuizFragmentToInsertQuesFragment()
            )
        )
    }
}