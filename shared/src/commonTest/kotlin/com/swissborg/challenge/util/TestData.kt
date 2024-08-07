package com.swissborg.challenge.util

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.swissborg.challenge.data.scheme.TradingPairScheme
import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.model.TradingPair

internal val TestTradingPairJson = """
    ["tBTCUSD",1,1,1,1,1,1,1,1,1,1]
""".trimIndent()

internal val TestTradingPairsJson =
    "[$TestTradingPairJson]"

internal val TestSymbol = Symbol(key = "tBTCUSD")

internal val TestTradingPairScheme =
    TradingPairScheme(
        symbol = TestSymbol.key,
        bid = "1",
        bidSize = "1",
        ask = "1",
        askSize = "1",
        dailyChange = "1",
        dailyChangeRelative = "1",
        lastPrice = "1",
        volume = "1",
        high = "1",
        low = "1",
    )

internal val TestTradingPair =
    TradingPair(
        symbol = TestSymbol,
        bid = 1f.toBigDecimal(),
        bidSize = 1f.toBigDecimal(),
        ask = 1f.toBigDecimal(),
        askSize = 1f.toBigDecimal(),
        dailyChange = 1f.toBigDecimal(),
        dailyChangeRelative = 1f.toBigDecimal(),
        lastPrice = 1f.toBigDecimal(),
        volume = 1f.toBigDecimal(),
        high = 1f.toBigDecimal(),
        low = 1f.toBigDecimal(),
    )
