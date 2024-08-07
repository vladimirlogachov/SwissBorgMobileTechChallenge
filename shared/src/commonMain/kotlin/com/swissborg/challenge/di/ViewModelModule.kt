package com.swissborg.challenge.di

import com.swissborg.challenge.presentation.main.MainViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val ViewModelModule = module {
    viewModel { MainViewModel(observeTradingPairs = get(), fetchTradingPairs = get()) }
}
