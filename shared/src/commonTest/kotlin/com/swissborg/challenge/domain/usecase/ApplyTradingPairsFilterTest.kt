package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.repository.TickersRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ApplyTradingPairsFilterTest {

    private val repository = mock<TickersRepository> {
        everySuspend { applyFilter(query = any()) } returns Unit
    }

    private val useCase = ApplyTradingPairsFilter(repository = repository)

    @Test
    fun `called with given query`() = runTest {
        val query = "BTC"
        useCase(query = query)
        verifySuspend(mode = VerifyMode.exactly(n = 1)) {
            repository.applyFilter(query = query)
        }
    }

}
