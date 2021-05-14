package com.quizzie.quizzieapp.binding

import android.view.View
import androidx.databinding.InverseMethod

object Converter {
    @JvmStatic fun floatToInt(
        value: Float
    ) = value.toInt()

    @InverseMethod("floatToInt")
    @JvmStatic fun intToFloat(
        value: Int
    ) = value.toFloat()
}