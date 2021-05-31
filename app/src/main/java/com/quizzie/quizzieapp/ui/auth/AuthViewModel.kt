package com.quizzie.quizzieapp.ui.auth

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.network.Errors
import com.quizzie.quizzieapp.network.RepoResult
import com.quizzie.quizzieapp.repository.AuthRepository
import com.quizzie.quizzieapp.ui.common.BaseViewEffect
import com.quizzie.quizzieapp.util.SingleLiveEvent
import com.quizzie.quizzieapp.ui.common.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val app: Application,
    private val authRepository: AuthRepository
) : ViewModel() {
    var email: String? = null
    var password: String? = null
    var loginJob: Job? = null
    var isLoggingIn = MutableLiveData(false)
    var viewEffect = SingleLiveEvent<BaseViewEffect>()

    fun onLoginButtonClick() {
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            viewEffect.setValue(
                BaseViewEffect.ShowSnackBar(
                    Snackbar(app.getString(R.string.empty_email_pass))
                )
            )
            return
        }

        loginJob = viewModelScope.launch {
            loginJob?.cancel()
            isLoggingIn.value = true
            val resp = authRepository.adminLogin(email!!.trim(), password?.trim()!!)
            isLoggingIn.value = false

            if (resp is RepoResult.Error) {
                viewEffect.setValue(
                    BaseViewEffect.ShowSnackBar(
                        Snackbar(
                            app.getString(
                                when (resp.err) {
                                    Errors.NetworkError -> R.string.network_error
                                    AuthRepository.AuthError.InvalidCred -> R.string.invalid_cred
                                    AuthRepository.AuthError.EmailUnver -> R.string.email_not_ver
                                    else -> R.string.unknown_error
                                }
                            )
                        )
                    )
                )
            }
        }
    }

}