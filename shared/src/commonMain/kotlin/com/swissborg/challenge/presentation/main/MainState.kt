package com.swissborg.challenge.presentation.main

import com.swissborg.challenge.domain.model.ConnectionState
import com.swissborg.challenge.domain.model.TradingPair

internal data class MainState(
    val tradingPairs: List<TradingPair> = emptyList(),
    val connectionState: ConnectionState = ConnectionState.Connected,
)
