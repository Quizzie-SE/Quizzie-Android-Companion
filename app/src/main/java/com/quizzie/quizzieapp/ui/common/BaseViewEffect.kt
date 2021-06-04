package com.quizzie.quizzieapp.ui.common

import androidx.navigation.NavDirections

open class BaseViewEffect {
    data class ShowSnackBar(val snackBar: Snackbar) : BaseViewEffect()
    data class NavigateTo(val navDirections: NavDirections) : BaseViewEffect()
    object NavigationPop : BaseViewEffect()
    data class ShowAlert(val alert: Alert) : BaseViewEffect()
    class SetFragmentResult(val key: String, vararg val pair: Pair<String, Any?>) : BaseViewEffect()
}


