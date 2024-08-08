package com.swissborg.challenge.domain.formatter

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.swissborg.challenge.domain.model.Currency
import kotlin.test.Test
import kotlin.test.assertEquals

class BigDecimalFormatterTest {

    @Test
    fun `validate daily change format`() {
        assertEquals(
            expected = "+3.19%",
            actual = 0.03186326.toBigDecimal().dailyChangeFormat(),
            message = "Wrong format, `+#.##%` expected",
        )
        assertEquals(
            expected = "+31.87%",
            actual = 0.3186326.toBigDecimal().dailyChangeFormat(),
            message = "Wrong format, `+#.##%` expected",
        )
        assertEquals(
            expected = "0.00%",
            actual = 0.0.toBigDecimal().dailyChangeFormat(),
            message = "Wrong format, `#.##%` expected",
        )
        assertEquals(
            expected = "-3.18%",
            actual = (-0.03186326).toBigDecimal().dailyChangeFormat(),
            message = "Wrong format, `+#.##%` expected",
        )
        assertEquals(
            expected = "-31.86%",
            actual = (-0.3186326).toBigDecimal().dailyChangeFormat(),
            message = "Wrong format, `+#.##%` expected",
        )
    }

    @Test
    fun `validate last price format with default currency`() {
        assertEquals(
            expected = "$3.19",
            actual = 3.19.toBigDecimal().lastPriceFormat(),
            message = "Wrong format, `$#.##` expected",
        )
        assertEquals(
            expected = "$0.02467",
            actual = 0.02467.toBigDecimal().lastPriceFormat(),
            message = "Wrong format, `$#.#####` expected",
        )
    }

    @Test
    fun `validate last price format with given currency`() {
        assertEquals(
            expected = "€3.19",
            actual = 3.19.toBigDecimal().lastPriceFormat(currency = Currency.EUR),
            message = "Wrong format, `$#.##` expected",
        )
        assertEquals(
            expected = "€0.02467",
            actual = 0.02467.toBigDecimal().lastPriceFormat(currency = Currency.EUR),
            message = "Wrong format, `$#.#####` expected",
        )
    }

}
