package com.swissborg.challenge.domain.formatter

import com.swissborg.challenge.domain.model.Currency
import com.swissborg.challenge.domain.model.Symbol

internal fun Symbol.format(quoteCurrency: Currency = Currency.USD): String =
    key.substring(startIndex = 1)
        .replace(oldValue = ":", newValue = "")
        .replace(oldValue = quoteCurrency.key, newValue = "/${quoteCurrency.key}")
