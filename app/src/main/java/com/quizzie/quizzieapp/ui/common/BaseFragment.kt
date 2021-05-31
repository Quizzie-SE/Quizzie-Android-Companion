package com.quizzie.quizzieapp.ui.common

import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.quizzie.quizzieapp.util.alert
import com.quizzie.quizzieapp.util.cast
import com.quizzie.quizzieapp.util.showSnackbar

abstract class BaseFragment: Fragment() {
    protected val navigator by lazy { findNavController() }
    protected val baseActivity: AppCompatActivity? by lazy { activity.cast() }

    fun handleBaseViewEffect(viewEffect: BaseViewEffect){
        when(viewEffect) {
            is BaseViewEffect.ShowSnackBar -> showSnackBar(viewEffect.snackBar)
            is BaseViewEffect.NavigateTo -> navigator.navigate(viewEffect.navDirections)
            is BaseViewEffect.NavigationPop -> navigator.popBackStack()
            is BaseViewEffect.ShowAlert -> requireContext().alert(viewEffect.alert)
            is BaseViewEffect.SetFragmentResult -> setFragmentResult(viewEffect.key, bundleOf(*viewEffect.pair))
        }
    }

    fun setActionBarTitle(title: String) {
        baseActivity?.supportActionBar?.title = title
    }

    fun showSnackBar(snackbar: Snackbar) {
        baseActivity?.findViewById<ViewGroup>(android.R.id.content)?.get(0)?.let { showSnackbar(it, snackbar) }
    }

}