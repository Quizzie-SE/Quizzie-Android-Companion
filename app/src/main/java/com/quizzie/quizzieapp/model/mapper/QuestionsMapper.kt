package com.quizzie.quizzieapp.model.mapper

import com.quizzie.quizzieapp.model.data.OptionResponse
import com.quizzie.quizzieapp.model.data.QuestionResponse
import com.quizzie.quizzieapp.model.domain.Question
import javax.inject.Inject

class QuestionsMapper @Inject constructor() : Mapper<Question, QuestionResponse>() {
    override fun domainToData(domainModel: Question) = with(domainModel) {
        QuestionResponse(
            qid, "", question, options.map { OptionResponse(it) }, options[correctOption]
        )
    }

    override fun dataToDomain(dataModel: QuestionResponse) = with(dataModel) {
        Question(
            _id, description, options.map { it.text }, options.indexOfFirst{ it.text == correctAnswer }
        )
    }

}