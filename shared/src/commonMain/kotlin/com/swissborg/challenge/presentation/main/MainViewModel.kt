package com.swissborg.challenge.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swissborg.challenge.domain.usecase.ApplyTradingPairsFilter
import com.swissborg.challenge.domain.usecase.FetchTradingPairs
import com.swissborg.challenge.domain.usecase.ObserveTradingPairs
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

internal class MainViewModel(
    observeTradingPairs: ObserveTradingPairs,
    private val fetchTradingPairs: FetchTradingPairs,
    private val applyTradingPairsFilter: ApplyTradingPairsFilter,
) : ViewModel() {

    private var refreshJob: Job? = null

    val intents = Channel<MainIntent>(capacity = Channel.UNLIMITED)
    val tradingPairs = observeTradingPairs()

    init {
        handleIntents()
    }

    private fun handleIntents() = viewModelScope.launch {
        intents.consumeAsFlow().collect { intent ->
            when (intent) {
                is MainIntent.FilterTradingPairs -> filterTradingPairs(query = intent.query)
                MainIntent.StartPeriodicRefresh -> startPeriodicRefresh()
                MainIntent.StopPeriodicRefresh -> stopPeriodicRefresh()
            }
        }
    }

    private fun filterTradingPairs(query: String) = viewModelScope.launch {
        applyTradingPairsFilter(query = query)
    }

    private fun startPeriodicRefresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            while (true) {
                fetchTradingPairs()
                delay(timeMillis = 5000)
            }
        }
    }

    private fun stopPeriodicRefresh() {
        refreshJob?.cancel()
    }

}
