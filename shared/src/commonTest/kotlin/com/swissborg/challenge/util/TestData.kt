package com.swissborg.challenge.util

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.swissborg.challenge.domain.model.TradingPair

internal val TestTradingPair =
    TradingPair(
        symbol = "tBTCUSD",
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
