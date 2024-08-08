package com.swissborg.challenge.domain.model

internal sealed interface ConnectionState {
    data object Disconnected : ConnectionState
    data object Connected : ConnectionState
}
