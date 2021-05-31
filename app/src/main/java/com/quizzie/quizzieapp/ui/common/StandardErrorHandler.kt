package com.quizzie.quizzieapp.ui.common

import android.content.Context
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.network.Errors
import com.quizzie.quizzieapp.util.SingleLiveEvent

class StandardErrorHandler(
    private val viewEffect: SingleLiveEvent<BaseViewEffect>,
    private val app: Context,
) {
    fun handle(e: Errors) {
        viewEffect.setValue(
            BaseViewEffect.ShowSnackBar(
                Snackbar(
                    app.getString(
                        when (e) {
                            Errors.NetworkError -> R.string.network_error
                            else -> R.string.unknown_error
                        }
                    )
                )
            )
        )
    }

}