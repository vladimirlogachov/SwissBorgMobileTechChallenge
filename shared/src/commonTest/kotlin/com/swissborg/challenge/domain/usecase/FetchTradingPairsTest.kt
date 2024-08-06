package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.repository.TickersRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class FetchTradingPairsTest {

    private val repository = mock<TickersRepository> {
        everySuspend { fetchTradingPairs(symbols = any()) } returns Unit
    }

    private val useCase = FetchTradingPairs(repository = repository)

    @Test
    fun `called with default symbols`() = runTest {
        useCase()
        verifySuspend(mode = VerifyMode.exactly(n = 1)) {
            repository.fetchTradingPairs(symbols = FetchTradingPairs.DEFAULT_SYMBOLS)
        }
    }

    @Test
    fun `called with given symbols`() = runTest {
        val symbols = listOf(Symbol(key = "tBTCUSD"))
        useCase(symbols = symbols)
        verifySuspend(mode = VerifyMode.exactly(n = 1)) {
            repository.fetchTradingPairs(symbols = symbols)
        }
    }

}
