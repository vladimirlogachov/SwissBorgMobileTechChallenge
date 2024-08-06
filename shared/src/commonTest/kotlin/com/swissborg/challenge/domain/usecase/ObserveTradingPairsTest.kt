package com.swissborg.challenge.domain.usecase

import app.cash.turbine.test
import com.swissborg.challenge.domain.model.TradingPair
import com.swissborg.challenge.domain.repository.TickersRepository
import com.swissborg.challenge.util.TestTradingPair
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveTradingPairsTest {

    private val testFlow = MutableStateFlow(value = listOf<TradingPair>())

    private val repository = mock<TickersRepository> {
        every { tradingPairs } returns testFlow as Flow<List<TradingPair>>
    }

    private val useCase = ObserveTradingPairs(repository = repository)

    @Test
    fun `returns flow of trading pairs with initial value`() = runTest {
        useCase().test {
            assertTrue(message = "Initial value is not empty") {
                awaitItem().isEmpty()
            }
        }
    }

    @Test
    fun `returns flow of trading pairs with updated value`() = runTest {
        val expected = listOf(TestTradingPair)

        useCase().test {
            skipItems(count = 1)
            testFlow.update { expected }
            assertEquals(
                expected = expected,
                actual = awaitItem(),
                message = "Trading pairs are not updated"
            )
        }
    }

}
