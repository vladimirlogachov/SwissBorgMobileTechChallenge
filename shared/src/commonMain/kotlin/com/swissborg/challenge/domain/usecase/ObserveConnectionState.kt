package com.swissborg.challenge.domain.usecase

import com.swissborg.challenge.domain.model.ConnectionState
import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.flow.map

internal class ObserveConnectionState(private val connectivity: Connectivity) {

    operator fun invoke() =
        connectivity.statusUpdates.map { state ->
            when (state) {
                is Connectivity.Status.Connected -> ConnectionState.Connected
                Connectivity.Status.Disconnected -> ConnectionState.Disconnected
            }
        }

}
