package com.quizzie.quizzieapp.ui.common

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.quizzie.quizzieapp.util.Snackbar
import com.quizzie.quizzieapp.util.cast
import com.quizzie.quizzieapp.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment: Fragment() {
    protected val navigator by lazy { findNavController() }
    protected val baseActivity: AppCompatActivity? by lazy { super.getActivity().cast() }

    fun handleBaseViewEffect(viewEffect: BaseViewEffect){
        when(viewEffect) {
            is BaseViewEffect.ShowSnackBar -> {
                showSnackBar(viewEffect.snackBar)
            }

            is BaseViewEffect.NavigateTo -> {
                navigator.navigate(viewEffect.navDirections)
            }

            is BaseViewEffect.NavigationPop -> {
                navigator.popBackStack()
            }
        }
    }

    fun setActionBarTitle(title: String) {
        baseActivity?.actionBar?.title = title
    }

    fun showSnackBar(snackbar: Snackbar) {
        baseActivity?.findViewById<ViewGroup>(android.R.id.content)?.get(0)?.let { showSnackbar(it, snackbar) }
    }

}