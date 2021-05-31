package com.quizzie.quizzieapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.ActivityLoginBinding
import com.quizzie.quizzieapp.isConnected
import com.quizzie.quizzieapp.repository.pref.SessionManager
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.ui.main.MainActivity
import com.quizzie.quizzieapp.ui.common.Snackbar
import com.quizzie.quizzieapp.util.showSnackbar
import com.google.android.material.snackbar.Snackbar as FSnackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var networkSnack: FSnackbar? = null
    @Inject lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        initObservers()
    }

    private fun initObservers() {
        viewModel.viewEffect.observe(this) {
            when(it) {
                is BaseViewEffect.ShowSnackBar -> showSnackbar(binding.root, it.snackBar)
            }
        }

        isConnected.asLiveData().observe(this) {
            if (!it) {
                networkSnack = showSnackbar(
                    binding.root, Snackbar(
                        getString(R.string.network_error),
                        length = FSnackbar.LENGTH_INDEFINITE,
                        bg = getColor(R.color.redColor)
                    )
                )
            } else {
                networkSnack?.dismiss()
            }
        }

        sessionManager.authStateNotifier.asLiveData().observe(this) {
            if (it) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

    }
}