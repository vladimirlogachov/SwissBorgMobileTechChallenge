package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.repository.TickersRepository

internal class ApplyTradingPairsFilter(private val repository: TickersRepository) {

    suspend operator fun invoke(query: String) =
        repository.applyFilter(query = query)

}
