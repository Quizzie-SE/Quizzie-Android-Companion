package com.quizzie.quizzieapp.service

import com.google.mlkit.vision.text.Text
import com.quizzie.quizzieapp.model.domain.Question
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

abstract class QuestionsParser(
    private val externalScope: CoroutineScope
){
    protected lateinit var question: String
    protected lateinit var options: MutableList<String>
    protected lateinit var parseOrder: MutableList<MCQStates>

    private val _outputFlow = MutableSharedFlow<Question>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val outputFlow: SharedFlow<Question>
        get() = _outputFlow

    private fun current(): MCQStates? {
        return if (parseOrder.isNotEmpty()) {
            parseOrder.last()
        } else null
    }

    private fun moveToNext() {
        if (parseOrder.isNotEmpty()) {
            parseOrder.removeLast()
        }
    }

    private fun reset() {
        question = ""
        options = mutableListOf("", "", "", "")

        parseOrder = mutableListOf(
            MCQStates.Options(),
            MCQStates.Question(),
            MCQStates.UnwantedText
        )
    }

    init {
        reset()
    }

    open fun parse(text: Text) {
        externalScope.launch {
            withContext(Dispatchers.Default) {
                reset()
                var blankLines = 0

                blocks@ for (block in text.textBlocks) {
                    blankLines = 1
                    lines@ for (line in block.lines) {
                        do {
                            val current = current() ?: break@blocks
                            val retVal = parse(line, current, blankLines)
                            if (retVal == Action.STATE || retVal == Action.LINE_STATE) moveToNext()
                            if (retVal == Action.EXIT) return@withContext

                        } while (retVal != Action.LINE && retVal != Action.LINE_STATE)

                        blankLines = 0
                    }
                }

                if (question.isNotBlank() && !options.contains("")) {
                    _outputFlow.emit(Question(question, options, 0))
                }
            }
        }
    }

    protected abstract fun parse(
        line: Text.Line,
        state: MCQStates,
        blankLinesBefore: Int
    ): Action

    enum class Action {
        LINE,
        STATE,
        LINE_STATE,
        EXIT,
        NONE
    }
}

