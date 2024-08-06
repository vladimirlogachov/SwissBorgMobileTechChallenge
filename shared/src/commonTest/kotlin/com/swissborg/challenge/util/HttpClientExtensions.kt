package com.swissborg.challenge.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json

private fun MockRequestHandleScope.respondOk(content: String): HttpResponseData =
    respond(
        status = HttpStatusCode.OK,
        headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
        content = content,
    )

internal fun mockClientSuccess(data: String): HttpClient =
    HttpClient(engine = MockEngine { respondOk(data) }) {
        expectSuccess = true
        install(ContentNegotiation) { json() }
    }

internal fun mockClientFailure(): HttpClient =
    HttpClient(engine = MockEngine { respondBadRequest() }) {
        expectSuccess = true
        install(ContentNegotiation) { json() }
    }
