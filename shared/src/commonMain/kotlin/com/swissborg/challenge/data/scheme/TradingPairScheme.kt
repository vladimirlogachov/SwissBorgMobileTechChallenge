package com.swissborg.challenge.data.scheme

import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.swissborg.challenge.data.serializer.TradingPairSchemeSerializer
import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.model.TradingPair
import kotlinx.serialization.Serializable

@Serializable(with = TradingPairSchemeSerializer::class)
data class TradingPairScheme(
    val symbol: String,
    val bid: String,
    val bidSize: String,
    val ask: String,
    val askSize: String,
    val dailyChange: String,
    val dailyChangeRelative: String,
    val lastPrice: String,
    val volume: String,
    val high: String,
    val low: String,
)

internal fun TradingPairScheme.toDomain(): TradingPair =
    TradingPair(
        symbol = Symbol(key = symbol),
        bid = bid.toBigDecimal(),
        bidSize = bidSize.toBigDecimal(),
        ask = ask.toBigDecimal(),
        askSize = askSize.toBigDecimal(),
        dailyChange = dailyChange.toBigDecimal(),
        dailyChangeRelative = dailyChangeRelative.toBigDecimal(),
        lastPrice = lastPrice.toBigDecimal(),
        volume = volume.toBigDecimal(),
        high = high.toBigDecimal(),
        low = low.toBigDecimal(),
    )
