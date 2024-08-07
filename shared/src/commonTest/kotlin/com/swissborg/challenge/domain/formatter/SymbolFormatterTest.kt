package com.swissborg.challenge.domain.formatter

import com.swissborg.challenge.domain.model.Currency
import com.swissborg.challenge.domain.model.Symbol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SymbolFormatterTest {

    @Test
    fun `test format with default quote currency`() {
        assertEquals(
            expected = "BTC/USD",
            actual = Symbol(key = "tBTCUSD").format(),
            message = "Expected format is {baseCurrency/quoteCurrency}",
        )
        assertEquals(
            expected = "CHSB/USD",
            actual = Symbol(key = "tCHSB:USD").format(),
            message = "Expected format is {baseCurrency/quoteCurrency}",
        )
    }

    @Test
    fun `test format with given quote currency`() {
        assertEquals(
            expected = "BTC/EUR",
            actual = Symbol(key = "tBTCEUR").format(quoteCurrency = Currency.EUR),
            message = "Expected format is {baseCurrency/quoteCurrency}",
        )
        assertEquals(
            expected = "CHSB/EUR",
            actual = Symbol(key = "tCHSB:EUR").format(quoteCurrency = Currency.EUR),
            message = "Expected format is {baseCurrency/quoteCurrency}",
        )
    }

    @Test
    fun `test format throws error for empty symbol`() {
        assertFailsWith<IndexOutOfBoundsException>(message = "Unknown exception type") {
            Symbol(key = "").format()
        }
    }

}
