package com.swissborg.challenge.domain.formatter

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.swissborg.challenge.domain.model.Currency

private val HUNDRED = 100.toBigDecimal()

internal fun BigDecimal.dailyChangeFormat(roundingMode: RoundingMode = RoundingMode.CEILING): String =
    multiply(other = HUNDRED)
        .roundToDigitPositionAfterDecimalPoint(digitPosition = 2, roundingMode = roundingMode)
        .run {
            when (signum()) {
                1 -> "+${toStringExpanded()}"
                -1 -> toStringExpanded()
                else -> "0.00"
            } + "%"
        }

internal fun BigDecimal.lastPriceFormat(currency: Currency = Currency.USD): String =
    "${currency.symbol}${toStringExpanded()}"
