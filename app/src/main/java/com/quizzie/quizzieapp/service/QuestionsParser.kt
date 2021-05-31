package com.quizzie.quizzieapp.service

import android.graphics.Rect
import com.google.mlkit.vision.text.Text
import com.quizzie.quizzieapp.model.domain.Question
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.nield.kotlinstatistics.standardDeviation
import timber.log.Timber

abstract class QuestionsParser(
    private val externalScope: CoroutineScope
) {
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

                val groupedBlocks = processBlocks(text.textBlocks)

                var blankLines = 0
                groups@ for (group in groupedBlocks) {
                    blocks@ for (block in group) {
                        blankLines = 1
                        lines@ for (line in block.lines) {
                            do {
                                val current = current() ?: break@blocks
                                val retVal = parse(line, current, blankLines)
                                if (retVal == Action.STATE || retVal == Action.LINE_STATE) moveToNext()
                                if (retVal == Action.EXIT) continue@groups

                            } while (retVal != Action.LINE && retVal != Action.LINE_STATE)

                            blankLines = 0
                        }
                    }

                    if (question.isNotBlank() && !options.contains("")) {
                        _outputFlow.emit(Question("", question, options, 0))
                        return@withContext
                    }
                    reset()
                }
            }
        }
    }

    private fun processBlocks(blocks: List<Text.TextBlock>): MutableList<List<Text.TextBlock>> {
        var bb = Rect()
        val processedBlocks = mutableListOf<List<Text.TextBlock>>()
        val values = mutableListOf<Int>()
        blocks.onEach {
            val diff = it.boundingBox?.top?.minus(bb.bottom)
            if (diff!! > 0) {
                values.add(diff)
            }
            bb = it.boundingBox!!
        }

        val avg = values.average()
        val std = values.standardDeviation()
        Timber.d("AVERAGE: $avg")
        Timber.d("STD: $std")

        bb = Rect()
        var currentBlock = mutableListOf<Text.TextBlock>()
        blocks.onEach {
            val diff = it.boundingBox?.top?.minus(bb.bottom)!!

            if (diff > 0 && diff > avg) {
                processedBlocks.add(currentBlock)
                currentBlock = mutableListOf()
            }

            currentBlock.add(it)
            bb = it.boundingBox!!
        }

        return processedBlocks
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

