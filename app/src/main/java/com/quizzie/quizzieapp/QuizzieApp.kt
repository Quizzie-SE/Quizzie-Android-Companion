package com.quizzie.quizzieapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

lateinit var PACKAGE_NAME: String
private val _isConnected = MutableStateFlow(false)
val isConnected: StateFlow<Boolean> = _isConnected

@HiltAndroidApp
class  QuizzieApp: Application() {
    private val connectivityManager by lazy { getSystemService(ConnectivityManager::class.java) }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        PACKAGE_NAME = packageName

        _isConnected.value = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        connectivityManager.registerDefaultNetworkCallback(ConnectivityCallback())
    }

    inner class ConnectivityCallback : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val connected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isConnected.value = connected
        }

        override fun onLost(network: Network) {
            if (connectivityManager.activeNetwork == null) {
                _isConnected.value = false
            }
        }
    }

}