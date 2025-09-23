package com.louisgautier.network

import com.louisgautier.apicontracts.dto.UserRefreshTokenJson
import com.louisgautier.apicontracts.dto.UserTokenJson
import com.louisgautier.apicontracts.routing.EndPoint
import com.louisgautier.network.interfaces.TokenAccessor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

internal class DefaultService(
    engine: HttpClientEngine = engineFactory.create(),
    tokenAccessor: TokenAccessor,
) {
    val unauthedClient = buildClient(engine)
    val authedClient =
        buildClient(engine) {
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenAccessor.getUserToken()
                        val refreshToken = tokenAccessor.getUserRefreshToken()
                        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                            null // No tokens available
                        } else {
                            BearerTokens(accessToken, refreshToken)
                        }
                    }

                    refreshTokens {
                        val oldRefreshToken =
                            UserRefreshTokenJson(tokenAccessor.getUserRefreshToken().orEmpty())

                        val newTokens =
                            call<UserTokenJson> {
                                unauthedClient.post(EndPoint.RefreshToken()) {
                                    setBody(oldRefreshToken)
                                }
                            }

                        if (newTokens.isSuccess) {
                            tokenAccessor.setUserToken(newTokens.getOrNull()!!.accessToken)
                            tokenAccessor.setUserRefreshToken(newTokens.getOrNull()!!.refreshToken)
                            BearerTokens(
                                newTokens.getOrNull()!!.accessToken,
                                newTokens.getOrNull()!!.refreshToken,
                            )
                        } else {
                            // Failed to refresh, clear tokens or trigger logout
                            tokenAccessor.removeUserToken()
                            null
                        }
                    }
                }
            }
        }
}
