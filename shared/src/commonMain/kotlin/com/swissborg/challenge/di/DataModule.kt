package com.swissborg.challenge.di

import com.swissborg.challenge.data.repository.RemoteTickersRepository
import com.swissborg.challenge.domain.repository.TickersRepository
import dev.jordond.connectivity.Connectivity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val DataModule = module {
    single { Connectivity { autoStart = true } }
    single {
        HttpClient {
            expectSuccess = true
            install(HttpCache)
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }
    single<TickersRepository> { RemoteTickersRepository(httpClient = get()) }
}
