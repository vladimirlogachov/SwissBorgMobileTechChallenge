package com.swissborg.challenge.presentation.main

internal sealed interface MainIntent {
    data object StartPeriodicRefresh : MainIntent
    data object StopPeriodicRefresh : MainIntent
    data class FilterTradingPairs(val query: String) : MainIntent
}
