package com.swissborg.challenge.data.repository

import com.swissborg.challenge.data.client.get
import com.swissborg.challenge.data.scheme.TradingPairScheme
import com.swissborg.challenge.data.scheme.toDomain
import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.model.TradingPair
import com.swissborg.challenge.domain.repository.TickersRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

internal class RemoteTickersRepository(private val httpClient: HttpClient) : TickersRepository {

    private val _tradingPairs =
        MutableStateFlow<List<TradingPair>>(value = emptyList())

    private val _filterQuery = MutableStateFlow(value = "")

    override val tradingPairs = combine(_tradingPairs, _filterQuery) { tradingPairs, filterQuery ->
        tradingPairs.filter { tradingPair ->
            tradingPair.symbol.key.contains(other = filterQuery, ignoreCase = true)
        }
    }

    override suspend fun fetchTradingPairs(symbols: List<Symbol>) = _tradingPairs.update {
        httpClient.get<List<TradingPairScheme>> {
            url(scheme = "https", host = "api-pub.bitfinex.com", path = "v2/tickers") {
                parameter(key = "symbols", value = symbols.asQueryParam())
            }
        }.map(TradingPairScheme::toDomain)
    }

    override suspend fun applyFilter(query: String) {
        _filterQuery.update { query }
    }

    internal companion object {

        fun List<Symbol>.asQueryParam(separator: CharSequence = ","): String =
            joinToString(separator = separator) { symbol -> symbol.key }

    }

}
