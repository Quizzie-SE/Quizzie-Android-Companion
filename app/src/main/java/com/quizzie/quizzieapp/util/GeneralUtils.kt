package com.quizzie.quizzieapp.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.format.DateUtils
import android.view.View
import android.view.ViewTreeObserver
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.quizzie.quizzieapp.R
import timber.log.Timber
import java.nio.ByteBuffer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val now
get() = Date().time

fun showSnackbar(
    anchor: View,
    snackbar: com.quizzie.quizzieapp.util.Snackbar
) = with(snackbar) {
    Snackbar.make(anchor, msg, length)
        .setTextColor(ContextCompat.getColor(anchor.context, android.R.color.white))
        .setBackgroundTint(ContextCompat.getColor(anchor.context, R.color.colorGrey))
        .setActionTextColor(ContextCompat.getColor(anchor.context, R.color.colorPrimary))
        .setAction(actionName) { action() }
        .show()
}

fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    val planeProxy = image.planes[0]
    val buffer: ByteBuffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun MediatorLiveData<Boolean>.addSource(vararg source: LiveData<Boolean>) {
    source.forEach { s ->
        addSource(s) { value = source.toList().any { it.value ?: false } }
    }
}

fun getSimpleRelativeDate(context: Context, unix: Long) = DateUtils.getRelativeDateTimeString(
    context,
    unix * 1000,
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.WEEK_IN_MILLIS,
    0
)

inline fun <reified T> Any?.cast(): T? {
    return this as? T
}

typealias Block = () -> Unit

inline fun Block.check(crossinline check: (Block) -> Unit): Block = { check(this) }

fun Long.toDate(format: String) = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun String.toLongDate(format: String) =
    SimpleDateFormat(format, Locale.getDefault()).parse(this)?.time

fun Context?.createDatePicker(
    liveData: MutableLiveData<String?>,
    dateFormat: String
): DatePickerDialog? {
    val dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val setDate = Calendar.getInstance().apply {
        try {
            dateFormatter.parse(liveData.value ?: dateFormatter.format(Date()))?.let {
                time = it
            }
        } catch (excep: ParseException) {
            Timber.e(excep)
        }
    }

    return with(setDate) {
        this@createDatePicker?.let {
            DatePickerDialog(
                it,
                { _, year, month, day ->
                    set(year, month, day)
                    liveData.value = dateFormatter.format(time)
                },
                get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}

fun Context?.createTimePicker(liveData: MutableLiveData<String?>, timeFormat: String): TimePickerDialog {
    val dateFormatter = SimpleDateFormat(timeFormat, Locale.getDefault())
    val setDate = Calendar.getInstance().apply {
        try {
            dateFormatter.parse(liveData.value ?: dateFormatter.format(Date()))?.let {
                time = it
            }
        } catch (excep: ParseException) {
            Timber.e(excep)
        }
    }

    return with(setDate) {
        TimePickerDialog(
            this@createTimePicker,
            { _, hour, min ->
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, min)
                liveData.value = dateFormatter.format(time)
            },
            get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE), false
        )
    }
}

inline fun View.afterMeasured(crossinline block: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block()
            }
        }
    })
}
