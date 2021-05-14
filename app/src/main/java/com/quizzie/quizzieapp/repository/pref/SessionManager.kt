package com.quizzie.quizzieapp.repository.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.MainThread
import com.google.gson.Gson
import com.quizzie.quizzieapp.model.domain.AuthToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class SessionManager @SuppressLint("CommitPrefEdits") @MainThread constructor(context: Context) {
    private val gson: Gson = Gson()
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val _authStateNotifier = MutableStateFlow(false)
    val authStateNotifier: StateFlow<Boolean> = _authStateNotifier

    private val _authToken: AuthToken?
        get() {
            val json: String? = pref.getString(AUTH_TOKEN, "")
            return gson.fromJson(json, AuthToken::class.java)
        }

    val authToken: AuthToken?
        get() {
            val token = _authToken
            if (token == null) {
                _authStateNotifier.value = false
            }
            return token
        }

    val authState: Boolean
        get() {
            val authToken: AuthToken? = _authToken
            return authToken != null
        }

//    val userDetails: User
//        get() {
//            val json: String = pref.getString(USER, "")
//            val user: User = gson.fromJson(json, User::class.java)
//            if (user == null) {
//                addToken(null)
//            }
//            return user
//        }
//
//    fun addUserDetails(user: User?) {
//        val json: String = gson.toJson(user)
//        editor.putString(USER, json)
//        editor.commit()
//        if (user == null) {
//            addToken(null)
//        }
//    }

    fun addToken(token: AuthToken?) {
        val json: String = gson.toJson(token)
        editor.putString(AUTH_TOKEN, json)
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
        Timber.d("SessionManager:")
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        _authStateNotifier.value = authState
        editor = pref.edit()
    }
}