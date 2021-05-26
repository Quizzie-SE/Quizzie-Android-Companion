package com.quizzie.quizzieapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class InputState<T>(
    input: T,
    vararg errorConditions: ErrorCondition<T>
) {
    private var isFirstTime = true
    private var currentError: ErrorCondition<T>? = null

    private val errorConditions = errorConditions.toMutableList()
    val input: MutableLiveData<T?> = MutableLiveData(input)
    val error = MutableLiveData("")
    val errorEnabled: LiveData<Boolean> = this.error.map { !it.isNullOrBlank() }

    init {

        this.input.observeForever {
            if (it != null && !(it is String && it.isEmpty())) {
                isFirstTime = false
            }
            for (err in errorConditions) {
                if (err.condition(it, isFirstTime)) {
                    this.error.value = err.errorMsg
                    currentError = err
                    break
                } else if (err === currentError) {
                    this.error.value = ""
                }
            }
        }
    }

    data class ErrorCondition<T>(
        val errorMsg: String,
        val condition: (input: T?, isFirstTime: Boolean) -> Boolean
    )

    companion object Conditions {
        val TEXT_REQUIRED = ErrorCondition<String>("This field is required")
        { input, isFirstTime ->
            input.isNullOrBlank() && !isFirstTime
        }

        fun BAD_DATE_FORMAT(format: String) = ErrorCondition<String>(
            "Enter in format ${format.toLowerCase(Locale.getDefault())}"
        )
        { input, _ ->
            val sdf: DateFormat = SimpleDateFormat(format, Locale.US)
            sdf.isLenient = false

            if (input == null) return@ErrorCondition false

            try {
                sdf.parse(input)
            } catch (e: ParseException) {
                return@ErrorCondition true
            }
            return@ErrorCondition false
        }

    }

}
