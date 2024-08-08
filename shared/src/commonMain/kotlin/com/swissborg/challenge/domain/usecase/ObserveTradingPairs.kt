package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.repository.TickersRepository

internal class ObserveTradingPairs(private val repository: TickersRepository) {

    operator fun invoke() =
        repository.tradingPairs

}
