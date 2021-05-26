package com.quizzie.quizzieapp.service

import com.google.mlkit.vision.text.Text
import com.quizzie.quizzieapp.model.domain.Question

class QuestionsParserImpl(todo: (Question) -> Unit) : QuestionsParser(todo) {

    private fun isUnwanted(line: String) = line.trim().startsWith("question", true)
    private fun isOption(line: String) = line.matches(Regex("""\(?\w[.)]\s.*"""))
    private fun getOptionChar(line: String) = """(?<=\(?)\w(?=[.)]\s)""".toRegex().find(line, 0)?.value
    private fun isCorrectUpcomingOption(line: String, state: MCQStates.Options): Boolean {
        return isOption(line) && state.optionType.match(state.optionNo + 1, getOptionChar(line) ?: "")
    }
    private fun isCorrectOption(line: String, state: MCQStates.Options): Boolean {
        return isOption(line) && state.optionType.match(state.optionNo, getOptionChar(line) ?: "")
    }

    override fun parse(line: Text.Line, state: MCQStates, blankLinesBefore: Int): Action {
        val lineText = line.text

        when (state) {
            is MCQStates.Options -> {

                if (isOption(lineText) && state.lineNo == 0 && state.optionNo == 0) {
                    state.optionType = OptionType.getOptionType(getOptionChar(lineText) ?: "") ?: OptionType.DECIMAL
                }

                if (isCorrectOption(lineText, state) || state.lineNo > 0) {
                    if (isCorrectUpcomingOption(lineText, state) && state.lineNo > 0) {
                        state.nextOption()
                    }
                    if (state.optionNo >= 4) return Action.EXIT

                    if (state.lineNo > 0) options[state.optionNo] += "\n".repeat(blankLinesBefore + 1)

                    options[state.optionNo] += if (isCorrectOption(lineText, state))
                        lineText.removePrefix(line.elements[0].text).trim() else lineText
                    state.nextLine()

                    return Action.LINE
                }
                return Action.EXIT
            }

            is MCQStates.Question -> {
                if (!isOption(lineText) || question.isBlank()) {
                    question += if (state.lineNo == 0 &&
                        lineText.matches("""\(?\d([.)])\s.*""".toRegex())
                    ) {
                        lineText.replaceFirst("""\(?\d([.)])\s""".toRegex(), "")
                    } else lineText

                    question += "\n".repeat(blankLinesBefore + 1)

                    state.nextLine()

                    if (isOption(lineText)) return Action.LINE_STATE
                }
                return if(isOption(lineText)) Action.STATE else Action.LINE
            }

            MCQStates.UnwantedText -> {
                return if(isUnwanted(lineText)) Action.LINE_STATE else Action.STATE
            }
        }

    }

}