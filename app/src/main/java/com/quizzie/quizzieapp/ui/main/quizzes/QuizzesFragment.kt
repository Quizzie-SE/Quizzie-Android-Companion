package com.quizzie.quizzieapp.ui.main.quizzes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentQuizzesBinding
import com.quizzie.quizzieapp.model.domain.Quiz
import com.quizzie.quizzieapp.ui.common.BaseFragment
import com.quizzie.quizzieapp.ui.custom.ItemDeleteMoveCallback
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.Mode
import com.quizzie.quizzieapp.util.CAPTURE_VALUE_KEY
import com.quizzie.quizzieapp.util.EDIT_QUIZ_VALUE_KEY
import com.quizzie.quizzieapp.util.RESULT
import com.quizzie.quizzieapp.util.VALUE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizzesFragment : BaseFragment() {
    private lateinit var binding: FragmentQuizzesBinding
    private val viewModel by viewModels<QuizzesViewModel>()
    private val quizzesAdapter = QuizzesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(
            EDIT_QUIZ_VALUE_KEY
        ) { key, res ->
            val mode = res.getParcelable<Mode>(RESULT)
            val quiz = res.getParcelable<Quiz>(VALUE)

            if (mode != null && quiz != null) {
                viewModel.setUpdate(mode, quiz)
            }
        }
    }

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
        binding.lifecycleOwner = viewLifecycleOwner
        initObservers()
        initRv()

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { viewModel.fetchAllQuizzes() }
            setColorSchemeColors(requireContext().getColor(R.color.colorPrimary))
        }
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

        viewModel.isFetching.observe(viewLifecycleOwner) {
            if (!it) binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun initRv() = binding.quizzesRv.apply {
        adapter = quizzesAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        object : ItemDeleteMoveCallback(context, false, true) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteQuiz(viewHolder.adapterPosition)
            }
        }.let {
            ItemTouchHelper(it)
        }.attachToRecyclerView(this)

        quizzesAdapter.onClickListener = {
            viewModel.selectQuiz(it)
        }
    }
}
