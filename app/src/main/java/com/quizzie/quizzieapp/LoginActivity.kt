package com.quizzie.quizzieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.quizzie.quizzieapp.ui.splash.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        login_btn.setOnClickListener {
            val intent = Intent(this, QuizzesActivity::class.java)
            startActivity(intent)
        }

        google_sign_in.setOnClickListener {
            val intent = Intent(this, QuizzesActivity::class.java)
            startActivity(intent)
        }
    }
}