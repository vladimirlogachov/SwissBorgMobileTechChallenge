package com.swissborg.challenge.domain.repository

import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.model.TradingPair
import kotlinx.coroutines.flow.Flow

internal interface TickersRepository {

    val tradingPairs: Flow<List<TradingPair>>

    suspend fun fetchTradingPairs(symbols: List<Symbol>)

}
