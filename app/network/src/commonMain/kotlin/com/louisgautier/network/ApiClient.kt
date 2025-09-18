package com.louisgautier.network

import com.louisgautier.apicontracts.getPlatform
import com.louisgautier.apicontracts.routing.Login
import com.louisgautier.apicontracts.routing.Protected
import com.louisgautier.apicontracts.routing.RefreshToken
import com.louisgautier.apicontracts.routing.Unprotected
import com.louisgautier.apicontracts.pojo.UserJson
import com.louisgautier.apicontracts.pojo.UserTokenJson
import com.louisgautier.apicontracts.routing.defaultJson
import com.louisgautier.core.AppLogger
import com.louisgautier.core.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
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
import kotlin.apply


class ApiClient(
    engine: HttpClientEngine = engineFactory.create(),
    preferences: AppPreferences,
) {

    val unauthedClient = buildClient(engine)
    val authedClient = buildClient(engine) {
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = preferences.getUserToken()
                    val refreshToken = preferences.getUserRefreshToken()
                    if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                        null // No tokens available
                    } else {
                        BearerTokens(accessToken, refreshToken)
                    }
                }

                refreshTokens {
                    val userToken = UserTokenJson(
                        preferences.getUserToken().orEmpty(),
                        preferences.getUserRefreshToken().orEmpty()
                    )
                    val newTokens = refresh(userToken)
                    if (newTokens.isSuccess) {
                        preferences.setUserToken(newTokens.getOrNull()!!.token)
                        preferences.setUserRefreshToken(newTokens.getOrNull()!!.refreshToken)
                        BearerTokens(
                            newTokens.getOrNull()!!.token,
                            newTokens.getOrNull()!!.refreshToken
                        )
                    } else {
                        // Failed to refresh, clear tokens or trigger logout
                        preferences.removeUserToken()
                        null
                    }
                }
            }
        }
    }

    suspend fun protected(): Result<String> {
        return call<String> { authedClient.get(Protected()) }
    }

    suspend fun unprotected(): Result<String> {
        return call<String> { unauthedClient.get(Unprotected()) }
    }

    suspend fun login(user: UserJson): Result<UserTokenJson> {
        return call<UserTokenJson> {
            unauthedClient.post(Login()) {
                setBody(user)
            }
        }
    }

    suspend fun refresh(token: UserTokenJson): Result<UserTokenJson> {
        return call<UserTokenJson> {
            unauthedClient.post(RefreshToken()) {
                setBody(token)
            }
        }
    }
}

private fun buildClient(
    engine: HttpClientEngine,
    config: HttpClientConfig<*>.() -> Unit = { }
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
        header("X-Platform", getPlatform().name)
        contentType(ContentType.Application.Json)
    }

    apply(config)
}


internal suspend inline fun <reified T> call(request: suspend () -> HttpResponse): Result<T> {
    return try {
        val response = request()
        Result.success(response.body<T>())
    } catch (e: Exception) {
        AppLogger.e(e.message)
        Result.failure(e)
    }
}