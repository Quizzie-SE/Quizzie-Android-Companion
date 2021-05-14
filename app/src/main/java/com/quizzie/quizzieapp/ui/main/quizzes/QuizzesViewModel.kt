package com.quizzie.quizzieapp.ui.main.quizzes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizzie.quizzieapp.di.Mock

import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.util.SingleLiveEvent
import com.quizzie.quizzieapp.util.check
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizzesViewModel @Inject constructor(
    val application: Application,
    @Mock private val quizzesRepository: QuizRepository
) : ViewModel() {

    val quizzes = MutableLiveData<List<Quiz>>()
    val viewEffect = SingleLiveEvent<BaseViewEffect>()

    init {
        fetchAllQuizzes()
    }

    fun createNewQuiz() = _selectQuiz(null)
    fun selectQuiz(quiz: Quiz) = _selectQuiz(quiz)
    fun selectQuiz(index: Int) = _selectQuiz(quizzes.value?.get(index))

    fun fetchAllQuizzes() = viewModelScope.launch {
        quizzes.value = quizzesRepository.getAllQuizzes()
    }

    private fun _selectQuiz(quiz: Quiz?) {
        viewEffect.setValue(BaseViewEffect.NavigateTo(QuizzesFragmentDirections
            .actionQuizzesFragmentToCreateQuizGraph().apply { this.quiz = quiz }))
    }

}