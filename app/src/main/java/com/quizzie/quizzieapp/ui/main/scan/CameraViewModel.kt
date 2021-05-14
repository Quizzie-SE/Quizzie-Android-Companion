package com.quizzie.quizzieapp.ui.main.scan

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    val application: Application
) : ViewModel() {

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { perm ->
        application.let {
            ContextCompat.checkSelfPermission(it, perm)
        } == PackageManager.PERMISSION_GRANTED
    }



    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}