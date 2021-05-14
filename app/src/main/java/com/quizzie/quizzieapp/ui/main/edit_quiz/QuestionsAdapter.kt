package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.quizzie.quizzieapp.databinding.ItemQuestionBinding
import com.quizzie.quizzieapp.model.domain.Question

class QuestionsAdapter(
    var onClickListener: (pos: Int) -> Unit = {}
): ListAdapter<Question, QuestionsAdapter.QuestionsVH>(Question.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsVH {
        return QuestionsVH(ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: QuestionsVH, position: Int) {
        holder.bind(getItem(position).question, position)
    }

    inner class QuestionsVH(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onClickListener(adapterPosition) }
        }

        fun bind(ques: String, index: Int) {
            binding.question = ques
            binding.number = index + 1
            binding.executePendingBindings()
        }
    }

}