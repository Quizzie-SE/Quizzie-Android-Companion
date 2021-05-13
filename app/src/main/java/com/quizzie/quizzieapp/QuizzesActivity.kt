package com.quizzie.quizzieapp

import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class QuizzesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        val toolbar: MaterialToolbar = findViewById<Toolbar>(R.id.toolbar) as MaterialToolbar
        setSupportActionBar(toolbar)
    }
}