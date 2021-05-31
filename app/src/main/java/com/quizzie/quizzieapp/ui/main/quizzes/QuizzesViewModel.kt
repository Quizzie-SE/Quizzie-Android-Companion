package com.quizzie.quizzieapp.ui.main.quizzes

import android.app.Application
import android.view.Display
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar as FSnackbar
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.di.Mock
import com.quizzie.quizzieapp.di.Production
import com.quizzie.quizzieapp.isConnected
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.ui.common.Snackbar
import com.quizzie.quizzieapp.ui.common.StandardErrorHandler
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.Mode
import com.quizzie.quizzieapp.util.SingleLiveEvent
import com.quizzie.quizzieapp.util.check
import com.quizzie.quizzieapp.util.editListLD
import com.quizzie.quizzieapp.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class QuizzesViewModel @Inject constructor(
    val application: Application,
    @Production private val quizzesRepository: QuizRepository
) : ViewModel() {

    var isFetching = MutableLiveData(false)
    private var quizzesJob: Job? = null
    val quizzes = MutableLiveData<List<Quiz>>()
    val viewEffect = SingleLiveEvent<BaseViewEffect>()
    val errorHandler = StandardErrorHandler(viewEffect, application)

    init {
        fetchAllQuizzes()
        viewModelScope.launch {
            isConnected.collect {
                if (it && quizzes.value?.isEmpty() == true) {
                    fetchAllQuizzes()
                }
            }
        }
    }

    fun createNewQuiz() = _selectQuiz(null)
    fun selectQuiz(quiz: Quiz) = _selectQuiz(quiz)
    fun selectQuiz(index: Int) = _selectQuiz(quizzes.value?.get(index))

    fun fetchAllQuizzes() {
        quizzesJob?.cancel()
        quizzesJob = viewModelScope.launch {
            isFetching.value = true
            quizzesRepository.getAllQuizzes().let {
                when (it) {
                    is RepoResult.Success -> quizzes.value = it.res
                    is RepoResult.Error -> errorHandler.handle(it.err)
                }
            }
            isFetching.value = false
        }
    }

    fun deleteQuiz(index: Int) {
        quizzes.value?.get(index)?.let { quiz ->
            quizzes.editListLD { removeAt(index) }
            val undoBlock = { quizzes.editListLD { add(index, quiz) } }
            val todoBlock = {
                viewModelScope.launch {
                    quizzesRepository.deleteQuiz(quiz).let {
                        if (it is RepoResult.Error) {
                            errorHandler.handle(it.err)
                            undoBlock()
                        }
                    }
                }
                Unit
            }

            viewEffect.setValue(
                BaseViewEffect.ShowSnackBar(
                    Snackbar(
                        application.getString(R.string.item_removed),
                        application.getString(R.string.undo),
                        undoBlock,
                        FSnackbar.LENGTH_LONG,
                        todoBlock
                    )
                )
            )
        }
    }

    private fun _selectQuiz(quiz: Quiz?) {
        viewEffect.setValue(
            BaseViewEffect.NavigateTo(QuizzesFragmentDirections
                .actionQuizzesFragmentToCreateQuizGraph().apply { this.quiz = quiz })
        )
    }

    fun setUpdate(mode: Mode, quiz: Quiz) {
        quizzes.editListLD {
            if (mode == Mode.EDIT) {
                removeIf { quiz.quizId == it.quizId }
            }
            add(quiz)
            sortBy { (it.scheduledFor - now).let { if (it > 0) it else Long.MAX_VALUE } }
        }
        viewEffect.setValue(BaseViewEffect.ShowSnackBar(Snackbar(application.getString(R.string.saved_success))))
    }

}