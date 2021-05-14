package com.quizzie.quizzieapp.service

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.quizzie.quizzieapp.model.domain.Question
import timber.log.Timber

class QuestionsAnalyser(val onSuccess: (text: String) -> Unit): ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            val recognizer = TextRecognition.getClient()
            recognizer.process(inputImage)
                .addOnSuccessListener { onSuccess(parse(it)) }
                .addOnFailureListener { Timber.e(it) }
                .addOnCompleteListener { image.close() }
        }
    }


    private fun parse(text: Text): String {
        var sText = ""
        for (block in text.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                    sText+=elementText
                }
            }
        }
        return sText
    }
}