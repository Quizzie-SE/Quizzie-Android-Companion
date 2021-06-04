package com.quizzie.quizzieapp.ui.main

import android.app.Fragment
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.ActivityMainBinding
import com.quizzie.quizzieapp.isConnected
import com.quizzie.quizzieapp.repository.pref.SessionManager
import com.quizzie.quizzieapp.ui.auth.LoginActivity
import com.quizzie.quizzieapp.util.cast
import com.quizzie.quizzieapp.util.hideSystemUI
import com.quizzie.quizzieapp.util.showSnackbar
import com.quizzie.quizzieapp.util.showSystemUI
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var networkSnack: Snackbar? = null
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMainBinding
    private val navigator by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            .cast<NavHostFragment>()?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.mainToolbar)

        navigator?.let {
            binding.mainToolbar.setupWithNavController(it, AppBarConfiguration(it.graph))
        }

        initObservers()

        navigator?.addOnDestinationChangedListener { _, navDestination, _ ->
            with(R.id.cameraFragment) {
                if (navDestination.id == this) {
                    supportActionBar?.hide()
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    hideSystemUI()
                } else {
                    supportActionBar?.show()
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                    showSystemUI()
                }
            }
        }
    }

    private fun initObservers() {
        sessionManager.authStateNotifier.asLiveData().observe(this) {
            if (!it) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
            }
        }

        isConnected.asLiveData().observe(this) {
            if (!it) {
                networkSnack = showSnackbar(
                    binding.root, com.quizzie.quizzieapp.ui.common.Snackbar(
                        getString(R.string.network_error),
                        length = Snackbar.LENGTH_INDEFINITE,
                        bg = getColor(R.color.redColor)
                    )
                )
            } else {
                networkSnack?.dismiss()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        menu?.findItem(R.id.profile)?.title = sessionManager.userDetails?.name
        MenuCompat.setGroupDividerEnabled(menu, true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signout -> {
                sessionManager.truncateSession()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}