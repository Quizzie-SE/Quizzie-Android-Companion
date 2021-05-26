package com.quizzie.quizzieapp.ui.main.scan

import android.Manifest
import android.content.Context
import android.media.MediaActionSound
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.graphics.rotationMatrix
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.load.ImageHeaderParser
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentCameraBinding
import com.quizzie.quizzieapp.service.QuestionsAnalyser
import com.quizzie.quizzieapp.ui.common.BaseFragment
import com.quizzie.quizzieapp.util.SETTINGS_PERM_INTENT
import com.quizzie.quizzieapp.util.Snackbar
import com.quizzie.quizzieapp.util.showSnackbar
import com.quizzie.quizzieapp.util.vibrate
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


@AndroidEntryPoint
class CameraFragment : BaseFragment() {
    private var analyser: QuestionsAnalyser? = null
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraPermRequester: ActivityResultLauncher<String>
    private var hasDispatchedOutput = false

    private val orientationEventListener by lazy {
        object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ImageHeaderParser.UNKNOWN_ORIENTATION) {
                    return
                }

                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                analyser?.rotation = rotation
                Timber.d(rotation.toString())

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraPermRequester =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    startCamera()
                } else {
                    showSnackbar(
                        binding.root, Snackbar(getString(R.string.camera_perm_req),
                            actionName = getString(R.string.allow),
                            action = {
                                startActivity(SETTINGS_PERM_INTENT)
                            })
                    )
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    private fun startCamera() {
        context?.let { LifecycleCameraController(it) }?.apply {
            analyser = QuestionsAnalyser(rotation = 0) {
                it?.let{
                    if (!hasDispatchedOutput) {
                        setFragmentResult(
                            CAPTURE_VALUE_KEY,
                            bundleOf("VALUE" to it))

                        hasDispatchedOutput = true
                        context?.vibrate()
                        navigator.popBackStack()
                    }
                }
            }

            setImageAnalysisAnalyzer(cameraExecutor, analyser!!)
            bindToLifecycle(viewLifecycleOwner)
            initializationFuture.addListener({
                analyser?.cameraInfo = cameraInfo
            }, ContextCompat.getMainExecutor(context))

            binding.previewView.controller = this
            isTapToFocusEnabled = true
            isPinchToZoomEnabled = true
        }
    }

    private fun requestPermission() {
        cameraPermRequester.launch(Manifest.permission.CAMERA)
    }

    companion object {
        const val CAPTURE_VALUE_KEY = "Capture"
    }

}