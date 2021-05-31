package com.quizzie.quizzieapp.ui.main.scan

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.quizzie.quizzieapp.service.QuestionsAnalyser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
@FlowPreview
@HiltViewModel
class CameraViewModel
@Inject constructor(
    val application: Application,
    val analyser: QuestionsAnalyser
) : ViewModel()