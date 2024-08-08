package com.swissborg.challenge.presentation.main

internal sealed interface MainAction {
    data object ShowProgress : MainAction
    data object HideProgress : MainAction
    data class ShowError(val message: String) : MainAction
}
