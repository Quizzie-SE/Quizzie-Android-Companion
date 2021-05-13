package com.quizzie.quizzieapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class QuizzesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizzes)

        /*val toolbar: MaterialToolbar = findViewById<Toolbar>(R.id.toolbar) as MaterialToolbar
        setSupportActionBar(toolbar)*/
        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#007BBA"))
        actionBar!!.setBackgroundDrawable(colorDrawable)
        supportActionBar!!.title = "Quizzes"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.logout) {
            // logout
        }
        return super.onOptionsItemSelected(item)
    }
}