package com.quizzie.quizzieapp.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.ui.common.Alert

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.alert(alert: Alert) {
    AlertDialog.Builder(this)
        .setTitle(alert.desp)
        .setPositiveButton(alert.yes) { _, _ -> alert.positiveAction() }
        .setNegativeButton(alert.no) { _, _ -> alert.negativeAction() }
        .show()
}

fun showSnackbar(
    anchor: View,
    snackbar: com.quizzie.quizzieapp.ui.common.Snackbar
) = with(snackbar) {
    return@with Snackbar.make(anchor, msg, length)
        .setTextColor(ContextCompat.getColor(anchor.context, R.color.white))
        .setActionTextColor(
            ContextCompat.getColor(
                anchor.context,
                R.color.colorPrimary
            )
        )
        .setAction(actionName) { action() }
        .setBackgroundTint(bg).apply { show() }
        .addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event != DISMISS_EVENT_ACTION) onDismiss()
            }
        })
}

fun Context.createCircularProgressDrawable() = CircularProgressDrawable(this).apply {
    strokeWidth = 5f
    centerRadius = 30F
    setColorSchemeColors(Color.WHITE)
    start()
}