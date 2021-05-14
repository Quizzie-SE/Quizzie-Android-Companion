package com.quizzie.quizzieapp.model.domain

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
    var quizName: String,
    var quizDate: Long,
    var duration: Long,
    var questions: List<Question> = listOf()
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Quiz>() {
            override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
                return oldItem.quizName == newItem.quizName
            }

            override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
                return oldItem == newItem
            }

        }
    }
}
