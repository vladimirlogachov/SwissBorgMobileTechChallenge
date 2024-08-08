package com.swissborg.challenge.domain.usecase

import app.cash.turbine.test
import com.swissborg.challenge.domain.model.ConnectionState
import dev.jordond.connectivity.Connectivity
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

class ObserveConnectionStateTest {

    private val connectivity = mock<Connectivity>()

    private val useCase = ObserveConnectionState(connectivity = connectivity)

    @Test
    fun `emits connected state when connected`() = runTest {
        every {
            connectivity.statusUpdates
        } returns MutableStateFlow(value = Connectivity.Status.Connected(metered = false))
        useCase().test {
            assertIs<ConnectionState.Connected>(
                value = awaitItem(),
                message = "Wrong state emitted",
            )
        }
    }

    @Test
    fun `emits disconnected state when disconnected`() = runTest {
        every {
            connectivity.statusUpdates
        } returns MutableStateFlow(value = Connectivity.Status.Disconnected)
        useCase().test {
            assertIs<ConnectionState.Disconnected>(
                value = awaitItem(),
                message = "Wrong state emitted",
            )
        }
    }

}
