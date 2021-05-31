package com.quizzie.quizzieapp.repository.impl


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.quizzie.quizzieapp.di.DispatcherIO
import com.quizzie.quizzieapp.model.domain.Question
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.model.mapper.QuestionsMapper
import com.quizzie.quizzieapp.network.BackendService
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.network.safeApiCall
import com.quizzie.quizzieapp.repository.QuizRepository
import com.quizzie.quizzieapp.util.now
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Singleton
class QuizRepositoryImpl @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val backendService: BackendService,
    private val questionsMapper: QuestionsMapper
) : QuizRepository {

    override suspend fun createQuiz(quiz: Quiz) = safeApiCall {
        val createdQuiz = backendService.createQuiz(quiz).result
        quiz.quizId = createdQuiz.quizId

        coroutineScope {
            questionsMapper.domainToData(quiz.questions).onEach {
                it.quizId = quiz.quizId
                launch { backendService.addQuestion(it) }
            }
        }
        return@safeApiCall quiz
    }

    override suspend fun deleteQuiz(quiz: Quiz) = safeApiCall {
        backendService.deleteQuiz(hashMapOf("quizId" to quiz.quizId))
    }

    override suspend fun updateQuiz(quiz: Quiz) = safeApiCall {
        val originalQuiz = backendService.getQuiz(quiz.quizId).result
        originalQuiz.questions =
            questionsMapper.dataToDomain(backendService.getAllQuestions(originalQuiz.quizId).result)

        coroutineScope {
            val questions = questionsMapper.domainToData(quiz.questions).onEach {
                it.quizId = quiz.quizId
            }

            quiz.questions.forEachIndexed { i, question ->
                if (question.qid.isBlank()) {
                    launch { backendService.addQuestion(questions[i]) }
                    return@forEachIndexed
                }
                val orgVariant = originalQuiz.questions.find { it.qid == question.qid }

                if (question != orgVariant) {
                    launch {
                        backendService.updateQuestion(
                            question.qid,
                            questions[i].getUpdateResponse()
                        )
                    }
                }
            }

            originalQuiz.questions.forEach { ques ->
                if (quiz.questions.find { it.qid ==  ques.qid} == null) {
                    backendService.deleteQuestion(ques.qid)
                }
            }

            if (
                quiz.quizName != originalQuiz.quizName ||
                quiz.scheduledFor != originalQuiz.scheduledFor ||
                quiz.quizDuration != originalQuiz.quizDuration
            ) {
                launch { backendService.updateQuiz(quiz.quizId, quiz.getUpdateResponse()) }
            }
        }
    }

    override suspend fun getAllQuizzes() = safeApiCall {
        backendService.getAllQuizzes().result.onEach {
            it.questions = questionsMapper.dataToDomain(
                backendService.getAllQuestions(it.quizId).result
            )
        }.sortedBy{  (it.scheduledFor - now).let { if (it > 0) it else Long.MAX_VALUE } }
    }

    private suspend fun <T> safeApiCall(call: suspend () -> T) = safeApiCall(dispatcher, call)
}