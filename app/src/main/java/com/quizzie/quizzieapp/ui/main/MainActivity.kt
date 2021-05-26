package com.quizzie.quizzieapp.ui.main

import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.load.ImageHeaderParser.UNKNOWN_ORIENTATION
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.ActivityMainBinding
import com.quizzie.quizzieapp.util.cast

import com.quizzie.quizzieapp.util.hideSystemUI
import com.quizzie.quizzieapp.util.showSystemUI
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewmodel by viewModels<MainViewModel>()
    private val navigator by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment).cast<NavHostFragment>()?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.mainToolbar)
        navigator?.let { binding.mainToolbar.setupWithNavController(it, AppBarConfiguration(it.graph)) }

        navigator?.addOnDestinationChangedListener{ _, navDestination, _->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.signout -> {
                //TODO: Sign Out
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}