package com.quizzie.quizzieapp.service

sealed class MCQStates {
    object UnwantedText : MCQStates()
    class Question : MCQStates() {
        var lineNo = 0

        fun nextLine() {
            lineNo++
        }
    }

    class Options : MCQStates() {
        var optionType = OptionType.DECIMAL

        var lineNo = 0
            private set

        var optionNo = 0
            private set

        fun nextOption() {
            lineNo = 0
            optionNo++
        }

        fun nextLine() {
            lineNo++
        }
    }
}

enum class OptionType(vararg nums: String) {
    ROMAN("i", "ii", "iii", "iv"),
    BIG_ROMAN("I", "II", "III", "IV"),
    DECIMAL("1", "2", "3", "4"),
    ALPHA("a", "b", "c", "d"),
    BIG_ALPHA("A", "B", "C", "D");

    private val nums: MutableMap<Int, String> = mutableMapOf()

    companion object {
        fun getOptionType(char: String): OptionType?{
            for(type in values()){
                if (char in type.nums.values) return type
            }
            return null
        }
    }

    fun match(no: Int, str: String) = nums[no] == str

    init {
        nums.forEachIndexed { i, s ->
            this.nums[i] = s
        }
    }
}

