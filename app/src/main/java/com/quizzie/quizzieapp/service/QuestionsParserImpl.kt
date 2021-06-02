package com.quizzie.quizzieapp.service

import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class QuestionsParserImpl @Inject constructor(
    externalScope: CoroutineScope
) : QuestionsParser(externalScope) {

    companion object {
        private const val STOP_SYMBOLS = ":.!?"
    }

    private fun isUnwanted(line: String) = line.trim().startsWith("question", true)
    private fun isOption(line: String) = line.matches(Regex("""\(?\w[.)]\s.*"""))
    private fun getOptionChar(line: String) =
        """(?<=\(?)\w(?=[.)]\s)""".toRegex().find(line, 0)?.value

    private fun isCorrectUpcomingOption(line: String, state: MCQStates.Options): Boolean {
        return isOption(line) && state.optionType.match(
            state.optionNo + 1,
            getOptionChar(line) ?: ""
        )
    }

    private fun isCorrectOption(line: String, state: MCQStates.Options): Boolean {
        return isOption(line) && state.optionType.match(state.optionNo, getOptionChar(line) ?: "")
    }

    private fun hasQnoPrefix(line: String) = line.matches("""\(?\d+([.)])\s.*""".toRegex())
    private fun removeQnoPrefix(line: String) =
        line.replaceFirst("""\(?\d+([.)])\s""".toRegex(), "")

    private fun manageNewLine(line: String, blankLinesBefore: Int, question: String) =
        (if (STOP_SYMBOLS.contains(question.last(), true)
        ) "\n".repeat(blankLinesBefore + 1) else " ") + line


    override fun parse(line: Text.Line, state: MCQStates, blankLinesBefore: Int): Action {
        val lineText = line.text

        when (state) {
            is MCQStates.Options -> {

                if (isOption(lineText) && state.lineNo == 0 && state.optionNo == 0) {
                    state.optionType = OptionType.getOptionType(getOptionChar(lineText) ?: "")
                        ?: OptionType.DECIMAL
                }

                if (isCorrectOption(lineText, state) || state.lineNo > 0) {
                    if (isCorrectUpcomingOption(lineText, state) && state.lineNo > 0) {
                        state.nextOption()
                    }
                    if (state.optionNo >= 4) return Action.EXIT

                    val processedLine = if (isCorrectOption(lineText, state))
                        lineText.removePrefix(line.elements[0].text).trim() else lineText
                    state.nextLine()

                    options[state.optionNo] += if (state.lineNo > 0) processedLine else manageNewLine(
                        processedLine,
                        blankLinesBefore,
                        question
                    )

                    return Action.LINE
                }
                return Action.EXIT
            }

            is MCQStates.Question -> {
                if (!isOption(lineText) || question.isBlank()) {
                    val processedLine = if (state.lineNo == 0 && hasQnoPrefix(lineText)) {
                        removeQnoPrefix(lineText)
                    } else lineText

                    question += if (question.isBlank()) processedLine else manageNewLine(
                        processedLine,
                        blankLinesBefore,
                        question
                    )

                    state.nextLine()
                    return Action.LINE
                }

                return Action.STATE
            }

            MCQStates.UnwantedText -> {
                return if (isUnwanted(lineText)) Action.LINE_STATE else Action.STATE
            }
        }

    }

}