package com.quizzie.quizzieapp.ui.auth

import android.view.View
import androidx.lifecycle.ViewModel
import com.quizzie.quizzieapp.repository.impl.AuthRepositoryImpl

class AuthViewModel : ViewModel() {

    var email: String? = null
    var password: String? = null

    var authListener: AuthListener? = null

    fun onLoginButtonClick(view: View) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid username or password")
            return
        }

        val loginResponse = AuthRepositoryImpl().adminLogin(email!!, password!!)
        authListener?.onSuccess(loginResponse)
    }

}