package com.quizzie.quizzieapp.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

data class Snackbar(
    val msg: String,
    val actionName: String = "",
    val action: () -> Unit = { },
    val length: Int = Snackbar.LENGTH_SHORT
)