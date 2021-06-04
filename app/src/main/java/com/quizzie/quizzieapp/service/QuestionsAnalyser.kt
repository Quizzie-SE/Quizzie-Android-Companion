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
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

@SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
@ExperimentalTime
@FlowPreview
@ViewModelScoped
class QuestionsAnalyser(
    var cameraInfo: CameraInfo? = null,
    var rotation: Int = 0,
    private val parser: QuestionsParser
) : ImageAnalysis.Analyzer {

    @Inject
    constructor(parser: QuestionsParserImpl) : this(null, 0, parser)

    private val ORIENTATIONS = SparseIntArray()

    fun getFlow() = parser.outputFlow
        .distinctUntilChanged()
        .debounce(1.2.seconds)

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