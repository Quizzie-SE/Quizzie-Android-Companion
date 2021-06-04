package com.quizzie.quizzieapp.model.domain

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize
import java.lang.IndexOutOfBoundsException

@Parcelize
data class Question(
    var qid: String,
    val question: String,
    val options: List<String>,
    val correctOption: Int
) : Parcelable {

    init {
        if (correctOption >= options.size) {
            throw IndexOutOfBoundsException("The correct Option Index should be in less than the size of options array")
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.question == newItem.question
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }
    }
}
