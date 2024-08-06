package com.swissborg.challenge.data.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

internal suspend inline fun <reified T> HttpClient.get(
    block: HttpRequestBuilder.() -> Unit = {}
) = get(builder = HttpRequestBuilder().apply(block = block)).body<T>()
