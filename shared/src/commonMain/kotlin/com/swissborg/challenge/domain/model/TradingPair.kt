package com.swissborg.challenge.domain.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

internal data class TradingPair(
    val symbol: String,
    val bid: BigDecimal,
    val bidSize: BigDecimal,
    val ask: BigDecimal,
    val askSize: BigDecimal,
    val dailyChange: BigDecimal,
    val dailyChangeRelative: BigDecimal,
    val lastPrice: BigDecimal,
    val volume: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
)
