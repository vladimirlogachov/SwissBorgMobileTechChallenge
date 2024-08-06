package com.swissborg.challenge.data.scheme

import com.swissborg.challenge.util.TestTradingPair
import com.swissborg.challenge.util.TestTradingPairScheme
import kotlin.test.Test
import kotlin.test.assertEquals

class TradingPairSchemeTest {

    @Test
    fun `map to domain`() {
        val expected = TestTradingPair
        val actual = TestTradingPairScheme.toDomain()
        assertEquals(
            expected = expected,
            actual = actual,
            message = "Trading pair is not properly mapped to domain"
        )
    }

}
