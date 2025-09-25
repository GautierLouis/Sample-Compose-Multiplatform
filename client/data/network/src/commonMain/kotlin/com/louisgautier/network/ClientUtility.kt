package com.louisgautier.network

import com.louisgautier.apicontracts.defaultJson
import com.louisgautier.logger.AppLogger
import com.louisgautier.platform.platform
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

internal fun buildClient(
    engine: HttpClientEngine,
    config: HttpClientConfig<*>.() -> Unit = { },
) = HttpClient(engine) {
    expectSuccess = true
    install(Resources)
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(defaultJson)
    }

    defaultRequest {
        url { protocol = URLProtocol.Companion.HTTP }
        host = "10.0.2.2"
        port = 8080
        header("X-Platform", platform())
        contentType(ContentType.Application.Json)
    }

    apply(config)
}

internal suspend inline fun <reified T> call(request: suspend () -> HttpResponse): Result<T> =
    try {
        val response = request()
        Result.success(response.body<T>())
    } catch (e: Exception) {
        AppLogger.e(e.message)
        Result.failure(e)
    }
