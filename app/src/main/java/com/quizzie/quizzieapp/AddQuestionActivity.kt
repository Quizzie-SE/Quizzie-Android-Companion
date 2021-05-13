package com.quizzie.quizzieapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_add_question.*

class AddQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#007BBA"))
        actionBar!!.setBackgroundDrawable(colorDrawable)
        supportActionBar!!.title = "Add Question"

        add_question_radio_option1.setOnClickListener {
            add_question_radio_option1.isChecked = true
            add_question_radio_option2.isChecked = false
            add_question_radio_option3.isChecked = false
            add_question_radio_option4.isChecked = false
        }
        add_question_radio_option2.setOnClickListener {
            add_question_radio_option1.isChecked = false
            add_question_radio_option2.isChecked = true
            add_question_radio_option3.isChecked = false
            add_question_radio_option4.isChecked = false
        }
        add_question_radio_option3.setOnClickListener {
            add_question_radio_option1.isChecked = false
            add_question_radio_option2.isChecked = false
            add_question_radio_option3.isChecked = true
            add_question_radio_option4.isChecked = false
        }
        add_question_radio_option4.setOnClickListener {
            add_question_radio_option1.isChecked = false
            add_question_radio_option2.isChecked = false
            add_question_radio_option3.isChecked = false
            add_question_radio_option4.isChecked = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu_main, menu)
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