package com.swissborg.challenge.di

import com.swissborg.challenge.domain.usecase.FetchTradingPairs
import com.swissborg.challenge.domain.usecase.ObserveTradingPairs
import org.koin.dsl.module

internal val UseCaseModule = module {
    factory { FetchTradingPairs(repository = get()) }
    factory { ObserveTradingPairs(repository = get()) }
}
