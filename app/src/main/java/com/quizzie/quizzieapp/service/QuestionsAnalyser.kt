package com.quizzie.quizzieapp.service

import android.annotation.SuppressLint
import android.util.SparseIntArray
import android.view.Surface
import androidx.camera.core.CameraInfo
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.quizzie.quizzieapp.model.domain.Question
import timber.log.Timber
import java.util.*

class QuestionsAnalyser(
    var cameraInfo: CameraInfo? = null,
    var rotation: Int,
    private val onSuccess: (Question?) -> Unit
) : ImageAnalysis.Analyzer {
    private val parser = QuestionsParserImpl(onSuccess)
    private val ORIENTATIONS = SparseIntArray()

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image

        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, getRotationCompensation())

            val recognizer = TextRecognition.getClient()
            recognizer.process(inputImage)
                .addOnSuccessListener { parser.parse(it) }
                .addOnFailureListener { Timber.e(it) }
                .addOnCompleteListener { image.close() }
        }
    }

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    private fun getRotationCompensation(): Int {
        var rotationCompensation = rotation.let { ORIENTATIONS.get(it) }
        val sensorRotation = cameraInfo?.sensorRotationDegrees
        rotationCompensation = ((sensorRotation?.minus(rotationCompensation) ?: 0) + 360) % 360
        return rotationCompensation
    }

}