package com.quizzie.quizzieapp.ui.main.edit_quiz

import android.os.Bundle
import android.view.*
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.navGraphViewModels
import com.quizzie.quizzieapp.R
import com.quizzie.quizzieapp.databinding.FragmentInsertQuesBinding
import com.quizzie.quizzieapp.ui.common.BaseFragment
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.Mode
import com.quizzie.quizzieapp.ui.main.edit_quiz.state.OnFragment
import com.quizzie.quizzieapp.ui.main.scan.CameraFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertQuesFragment : BaseFragment() {
    private lateinit var binding: FragmentInsertQuesBinding
    private val viewmodel by hiltNavGraphViewModels<EditQuizViewModel>(R.id.create_quiz_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        registerCameraResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertQuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewmodel.notifyOnFragment(OnFragment.ADD_QUES)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun registerCameraResult() {
        setFragmentResultListener(
            CameraFragment.CAPTURE_VALUE_KEY
        ) { key, res ->
            viewmodel.insertQuesViewState.ques = res.getParcelable("VALUE")
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.save).isVisible = viewmodel.isSaving.value?.not() ?: true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewmodel.save()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initObservers() {
        viewmodel.insertQuesViewState.mode.observe(viewLifecycleOwner) { mode ->
            setActionBarTitle(getString(if (mode == Mode.CREATE) R.string.create_ques else R.string.edit_ques))
        }

        viewmodel.isSaving.observe(viewLifecycleOwner) { baseActivity?.invalidateOptionsMenu() }

        viewmodel.viewEffect.observe(viewLifecycleOwner) {
            when (it) {
                else -> handleBaseViewEffect(it)
            }
        }

    }
}