package com.swissborg.challenge.di

import com.swissborg.challenge.domain.usecase.ApplyTradingPairsFilter
import com.swissborg.challenge.domain.usecase.FetchTradingPairs
import com.swissborg.challenge.domain.usecase.ObserveConnectionState
import com.swissborg.challenge.domain.usecase.ObserveTradingPairs
import org.koin.dsl.module

internal val UseCaseModule = module {
    factory { FetchTradingPairs(repository = get()) }
    factory { ObserveTradingPairs(repository = get()) }
    factory { ApplyTradingPairsFilter(repository = get()) }
    factory { ObserveConnectionState(connectivity = get()) }
}
