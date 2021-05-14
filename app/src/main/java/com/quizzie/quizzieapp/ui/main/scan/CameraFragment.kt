package com.quizzie.quizzieapp.ui.main.scan

import android.Manifest
import android.media.MediaActionSound
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentCameraBinding
import com.quizzie.quizzieapp.service.QuestionsAnalyser
import com.quizzie.quizzieapp.ui.common.BaseFragment
import com.quizzie.quizzieapp.util.SETTINGS_PERM_INTENT
import com.quizzie.quizzieapp.util.Snackbar
import com.quizzie.quizzieapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class CameraFragment : BaseFragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraPermRequester: ActivityResultLauncher<String>

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton2.setOnClickListener{
            requestPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    private fun startCamera() {
        context?.let { LifecycleCameraController(it) }?.apply {
            setImageAnalysisAnalyzer(cameraExecutor, QuestionsAnalyser {
                Timber.d(it)
            })
            bindToLifecycle(viewLifecycleOwner)
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