package com.swissborg.challenge.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swissborg.challenge.domain.model.ConnectionState
import com.swissborg.challenge.domain.usecase.ApplyTradingPairsFilter
import com.swissborg.challenge.domain.usecase.FetchTradingPairs
import com.swissborg.challenge.domain.usecase.ObserveConnectionState
import com.swissborg.challenge.domain.usecase.ObserveTradingPairs
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class MainViewModel(
    observeTradingPairs: ObserveTradingPairs,
    observeConnectionState: ObserveConnectionState,
    private val fetchTradingPairs: FetchTradingPairs,
    private val applyTradingPairsFilter: ApplyTradingPairsFilter,
) : ViewModel() {

    private val _intent = Channel<MainIntent>(capacity = Channel.UNLIMITED)
    private val _action = Channel<MainAction>(capacity = Channel.BUFFERED)
    private var refreshJob: Job? = null

    val action = _action.receiveAsFlow()
    val state = combine(
        observeTradingPairs(),
        observeConnectionState(),
    ) { tradingPairs, connectionState ->
        MainState(tradingPairs = tradingPairs, connectionState = connectionState)
            .also { state ->
                when (state.connectionState) {
                    ConnectionState.Connected -> startPeriodicRefresh()
                    ConnectionState.Disconnected -> stopPeriodicRefresh()
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MainState(),
    )

    init {
        collectUserIntents()
    }

    fun submitIntent(intent: MainIntent) {
        _intent.trySend(element = intent)
    }

    private fun collectUserIntents() = viewModelScope.launch {
        _intent.consumeAsFlow().collect { intent ->
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

    private fun fetchData() = viewModelScope.launch {
        while (true) {
            _action.trySend(element = MainAction.ShowProgress)
            fetchTradingPairs().onFailure { error ->
                if (error !is CancellationException) {
                    MainAction.ShowError(message = error.message ?: "Unknown error")
                        .run(block = _action::trySend)
                }
            }
            _action.trySend(element = MainAction.HideProgress)
            delay(timeMillis = REFRESH_INTERVAL)
        }
    }

    private fun startPeriodicRefresh() {
        refreshJob?.cancel()
        refreshJob = fetchData()
    }

    private fun stopPeriodicRefresh() {
        refreshJob?.cancel()
    }

    private companion object {
        const val REFRESH_INTERVAL = 5000L
    }

}
