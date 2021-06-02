package com.quizzie.quizzieapp.repository.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.MainThread
import androidx.core.graphics.rotationMatrix
import com.google.gson.Gson
import com.quizzie.quizzieapp.model.domain.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject @SuppressLint("CommitPrefEdits") constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val _authStateNotifier = MutableStateFlow(false)
    val authStateNotifier: StateFlow<Boolean>
        get() = _authStateNotifier

    private val _authToken: String?
        get() {
            return pref.getString(AUTH_TOKEN, "")
        }

    val authToken: String?
        get() {
            val token = _authToken
            if (token == null) {
                _authStateNotifier.value = false
            }
            return token
        }

    val authState: Boolean
        get() {
            val authToken: String? = _authToken
            return authToken != null
        }

    val userDetails: User?
        get() {
            val json: String? = pref.getString(USER, "")
            val user: User? = gson.fromJson(json, User::class.java)
            if (user == null) {
                truncateSession()
            }
            return user
        }

    fun addUserDetails(user: User?) {
        val json: String = gson.toJson(user)
        editor.putString(USER, json)
        editor.commit()
        if (user == null) {
            truncateSession()
        }
    }

    fun addToken(token: String?) {
        editor.putString(AUTH_TOKEN, token)
        editor.commit()
        _authStateNotifier.value = token != null
    }

    fun truncateSession() {
        editor.clear()
        editor.commit()
        _authStateNotifier.value = false
    }

    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREF_NAME = "UserSession"
        private const val AUTH_TOKEN = "AuthToken"
        private const val USER = "User"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        _authStateNotifier.value = authState
        editor = pref.edit()
    }
}