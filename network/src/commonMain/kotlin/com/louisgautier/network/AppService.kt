package com.louisgautier.network

import com.louisgautier.shared.Greeting
import com.louisgautier.shared.routing.Login
import com.louisgautier.shared.routing.Protected
import com.louisgautier.shared.routing.Unprotected
import com.louisgautier.shared.routing.User
import com.louisgautier.shared.routing.UserToken
import com.louisgautier.shared.routing.defaultJson
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

val baseHttpClientConfig: HttpClientConfig<*>.() -> Unit = {
    expectSuccess = true
    install(Resources)
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(defaultJson)
    }

    install(ResponseObserver) {
        onResponse { response ->
            println("HTTP status: $response")
        }
    }

    defaultRequest {
        url { protocol = URLProtocol.HTTP }
        host = "10.0.2.2"
        port = 8080
        header("X-Platform", Greeting().platform.name)
        contentType(ContentType.Application.Json)
    }
}


class AppService(
    private val unauthenticatedClient: HttpClient,
    private val authenticatedClient: HttpClient,
) {


    suspend fun unprotected(): Result<String> {
        return call<String> { unauthenticatedClient.get(Unprotected()) }
    }

    suspend fun protected(): Result<String> {
        return call<String> { authenticatedClient.get(Protected()) }
    }

    suspend inline fun <reified T> call(request: suspend () -> HttpResponse): Result<T> {
        return try {
            val response = request()
            Result.success(response.body<T>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(): Result<UserToken> {
        return call<UserToken> {
            unauthenticatedClient.post(Login()) {
                setBody(User("test", "test")) }
        }
    }
}