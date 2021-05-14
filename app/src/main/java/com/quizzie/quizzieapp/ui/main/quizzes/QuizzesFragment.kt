package com.quizzie.quizzieapp.ui.main.quizzes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentQuizzesBinding
import com.quizzie.quizzieapp.ui.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizzesFragment : BaseFragment() {
    private lateinit var binding: FragmentQuizzesBinding
    private val viewModel by viewModels<QuizzesViewModel>()
    private val quizzesAdapter = QuizzesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizzesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        initObservers()
        initRv()
    }

    private fun initObservers() {
        viewModel.viewEffect.observe(viewLifecycleOwner) {
            when (it) {
                else -> handleBaseViewEffect(it)
            }
        }

        viewModel.quizzes.observe(viewLifecycleOwner) {
            quizzesAdapter.submitList(it)
        }
    }

    private fun initRv() = binding.quizzesRv.apply {
        adapter = quizzesAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        quizzesAdapter.onClickListener = {
            viewModel.selectQuiz(it)
        }
    }
}
