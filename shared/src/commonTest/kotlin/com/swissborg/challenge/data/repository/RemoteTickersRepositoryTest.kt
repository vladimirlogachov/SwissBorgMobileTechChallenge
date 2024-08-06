package com.swissborg.challenge.data.repository

import app.cash.turbine.test
import com.swissborg.challenge.data.repository.RemoteTickersRepository.Companion.asQueryParam
import com.swissborg.challenge.domain.repository.TickersRepository
import com.swissborg.challenge.util.TestSymbol
import com.swissborg.challenge.util.TestTradingPair
import com.swissborg.challenge.util.TestTradingPairsJson
import com.swissborg.challenge.util.mockClientFailure
import com.swissborg.challenge.util.mockClientSuccess
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RemoteTickersRepositoryTest {

    private val repository: TickersRepository =
        RemoteTickersRepository(httpClient = mockClientSuccess(data = TestTradingPairsJson))

    @Test
    fun `returns flow of trading pairs with initial value`() = runTest {
        repository.tradingPairs.test {
            assertTrue(message = "Initial value is not empty") {
                awaitItem().isEmpty()
            }
        }
    }

    @Test
    fun `successful fetch updates trading pairs`() = runTest {
        val expected = listOf(TestTradingPair)
        repository.tradingPairs.test {
            skipItems(count = 1)
            repository.fetchTradingPairs(symbols = emptyList())
            val actual = awaitItem()
            assertEquals(
                expected = expected,
                actual = actual,
                message = "Trading pairs are not updated"
            )
        }
    }

    @Test
    fun `failed fetch throws exception`() = runTest {
        assertFailsWith<ClientRequestException>(message = "HttpExceptions are not thrown") {
            RemoteTickersRepository(httpClient = mockClientFailure())
                .fetchTradingPairs(symbols = emptyList())
        }
    }

    @Test
    fun `multiple symbols list transformed into comma separated string param`() {
        val expected = "${TestSymbol.key},${TestSymbol.key}"
        val actual = listOf(TestSymbol, TestSymbol).asQueryParam()
        assertEquals(
            expected = expected,
            actual = actual,
            message = "Invalid symbols query parameter",
        )
    }

    @Test
    fun `single symbol list transformed into string param`() {
        val expected = TestSymbol.key
        val actual = listOf(TestSymbol).asQueryParam()
        assertEquals(
            expected = expected,
            actual = actual,
            message = "Invalid symbols query parameter",
        )
    }

}
