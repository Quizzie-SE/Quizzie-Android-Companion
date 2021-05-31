package com.quizzie.quizzieapp.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.quizzie.quizzieapp.PACKAGE_NAME

const val BACKEND_BASE_URL = "https://quizzie-api.herokuapp.com/"
const val CAPTURE_VALUE_KEY = "Capture"
const val EDIT_QUIZ_VALUE_KEY = "Edit_Quiz"
const val RESULT = "RESULT"
const val VALUE = "VALUE"

const val DATE_FORMAT = "dd-MM-yyyy"
const val TIME_FORMAT = "hh:mm a"

val SETTINGS_PERM_INTENT
get() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
    data = Uri.fromParts("package", PACKAGE_NAME, null) }
