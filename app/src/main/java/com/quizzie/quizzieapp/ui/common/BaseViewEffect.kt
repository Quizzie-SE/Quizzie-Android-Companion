package com.quizzie.quizzieapp.ui.common

import androidx.navigation.NavDirections
import com.quizzie.quizzieapp.ui.main.quizzes.QuizzesFragmentDirections
import com.quizzie.quizzieapp.util.Snackbar

open class BaseViewEffect {
    data class ShowSnackBar(val snackBar: Snackbar): BaseViewEffect()
    data class NavigateTo(val navDirections: NavDirections): BaseViewEffect()
    object NavigationPop: BaseViewEffect()
}
