package com.quizzie.quizzieapp.model.domain

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import com.quizzie.quizzieapp.model.data.UpdateOps
import com.quizzie.quizzieapp.model.data.UpdateResponse
import com.quizzie.quizzieapp.util.now
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
    @SerializedName("_id")
    var quizId: String,
    var quizName: String,
    var scheduledFor: Long,
    var quizDuration: Long,
    var questions: List<Question> = listOf(),
    var quizType: QuizType = QuizType.Public
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Quiz>() {
            override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
                return oldItem.quizName == newItem.quizName
            }

            override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
                return oldItem == newItem
            }
        }

        fun List<Quiz>.sort() = { it: Quiz -> (it.scheduledFor - now) > 0 }.let { filter ->
            filter(filter).sortedBy { it.scheduledFor - now } +
                    filterNot(filter).sortedBy { it.scheduledFor - now }
        }
    }

    fun getUpdateResponse() = UpdateOps(
        listOf(
            UpdateResponse("quizName", quizName),
            UpdateResponse("scheduledFor", scheduledFor),
            UpdateResponse("quizDuration", quizDuration),
            UpdateResponse("quizType", quizType)
        )
    )

}
