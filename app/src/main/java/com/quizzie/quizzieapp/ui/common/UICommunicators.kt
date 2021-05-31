package com.quizzie.quizzieapp.ui.common

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar
import com.quizzie.quizzieapp.util.Block

data class Snackbar(
    val msg: String,
    val actionName: String = "",
    val action: Block = { },
    val length: Int = Snackbar.LENGTH_SHORT,
    val onDismiss: Block = { },
    @ColorInt val bg: Int = Color.BLACK
)

data class Alert(
    val title: String,
    val desp: String,
    val yes: String,
    val no: String,
    val positiveAction: Block,
    val negativeAction: Block
)