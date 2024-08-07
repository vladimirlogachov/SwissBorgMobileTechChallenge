package com.swissborg.challenge.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swissborg.challenge.domain.usecase.FetchTradingPairs
import com.swissborg.challenge.domain.usecase.ObserveTradingPairs
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val observeTradingPairs: ObserveTradingPairs,
    private val fetchTradingPairs: FetchTradingPairs,
) : ViewModel() {

    val tradingPairs = observeTradingPairs()

    init {
        viewModelScope.launch {
            fetchTradingPairs()
        }
    }

}
