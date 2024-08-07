package com.swissborg.challenge.domain.model

internal sealed class Currency(val key: String, val symbol: String) {
    data object USD : Currency(key = "USD", symbol = "$")
    data object EUR : Currency(key = "EUR", symbol = "€")
}
