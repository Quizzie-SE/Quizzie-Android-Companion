package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentCreateQuizBinding
import com.quizzie.quizzieapp.ui.common.BaseFragment
import com.quizzie.quizzieapp.ui.custom.ItemDeleteMoveCallback
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.Mode
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.OnFragment
import com.quizzie.quizzieapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CreateQuizFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateQuizBinding
    private val viewmodel by hiltNavGraphViewModels<EditQuizViewModel>(R.id.create_quiz_graph)
    private val questionsAdapter = QuestionsAdapter()
    private val args by navArgs<CreateQuizFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewmodel.notifyOnFragment(OnFragment.CREATE_QUIZ)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
        initRv()
        initObservers()
        initListeners()

        args.quiz?.let { viewmodel.initializeQuiz(it) }
    }

    private fun initListeners() {
        binding.quizDateEt.iconView.setOnClickListener {
            context.createDatePicker(viewmodel.createQuizViewState.date.input, DATE_FORMAT)?.show()
        }

        binding.quizTimeEt.iconView.setOnClickListener {
            context.createTimePicker(viewmodel.createQuizViewState.time.input, TIME_FORMAT).show()
        }
    }

    private fun initRv() = binding.questionsRv.apply {
        adapter = questionsAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        questionsAdapter.onClickListener = { viewmodel.selectQuestion(it) }

        object : ItemDeleteMoveCallback(context, false, true) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewmodel.removeQuestion(viewHolder.adapterPosition)
            }
        }.let {
            ItemTouchHelper(it)
        }.attachToRecyclerView(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.save).isVisible = viewmodel.isSaving.value == false
        menu.findItem(R.id.save_progress).isVisible = viewmodel.isSaving.value == true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewmodel.save()
            return true
        }
        return super.onContextItemSelected(item)
    }

    private fun initObservers() {
        viewmodel.createQuizViewState.mode.observe(viewLifecycleOwner) { mode ->
            setActionBarTitle(getString(if (mode == Mode.CREATE) R.string.create_quiz else R.string.edit_quiz))
        }

        viewmodel.createQuizViewState.questions.observe(viewLifecycleOwner) {
            questionsAdapter.submitList(it)
        }

        viewmodel.isSaving.observe(viewLifecycleOwner) {
            baseActivity?.invalidateOptionsMenu()
        }

        viewmodel.viewEffect.observe(viewLifecycleOwner) {
            when (it) {
                else -> handleBaseViewEffect(it)
            }
        }
    }

}


