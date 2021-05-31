package com.quizzie.quizzieapp.ui.main.quizzes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quizzie.quizzieapp.databinding.ItemQuizBinding
import com.quizzie.quizzieapp.model.domain.Quiz
import java.util.*

class QuizzesAdapter(
    var onClickListener: (pos: Int) -> Unit = {}
) : ListAdapter<Quiz, QuizzesAdapter.QuizVH>(Quiz.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizVH {
        return QuizVH(
            ItemQuizBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuizVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuizVH(private val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onClickListener(adapterPosition) }
        }

        fun bind(quiz: Quiz) {
            binding.quiz = quiz
            binding.isOver = Date().time > quiz.scheduledFor
            binding.executePendingBindings()
        }
    }

}