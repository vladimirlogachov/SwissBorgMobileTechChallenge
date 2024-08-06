package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.model.Symbol
import com.swissborg.challenge.domain.repository.TickersRepository

internal class FetchTradingPairs(private val repository: TickersRepository) {

    suspend operator fun invoke(symbols: List<Symbol> = DEFAULT_SYMBOLS) =
        repository.fetchTradingPairs(symbols = symbols)

    companion object {

        val DEFAULT_SYMBOLS = listOf(
            Symbol(key = "tBTCUSD"),
            Symbol(key = "tETHUSD"),
            Symbol(key = "tCHSB:USD"),
            Symbol(key = "tLTCUSD"),
            Symbol(key = "tXRPUSD"),
            Symbol(key = "tDSHUSD"),
            Symbol(key = "tRRTUSD"),
            Symbol(key = "tEOSUSD"),
            Symbol(key = "tSANUSD"),
            Symbol(key = "tDATUSD"),
            Symbol(key = "tSNTUSD"),
            Symbol(key = "tDOGE:USD"),
            Symbol(key = "tLUNA:USD"),
            Symbol(key = "tMATIC:USD"),
            Symbol(key = "tNEXO:USD"),
            Symbol(key = "tOCEAN:USD"),
            Symbol(key = "tBEST:USD"),
            Symbol(key = "tAAVE:USD"),
            Symbol(key = "tPLUUSD"),
            Symbol(key = "tFILUSD"),
        )

    }

}
