package com.louisgautier.network

import com.louisgautier.apicontracts.dto.UserJson
import com.louisgautier.apicontracts.dto.UserRefreshTokenJson
import com.louisgautier.apicontracts.dto.UserTokenJson
import com.louisgautier.apicontracts.routing.EndPoint
import com.louisgautier.network.interfaces.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.setBody

internal class DefaultAuthService(
    private val client: HttpClient,
) : AuthService {
    override suspend fun registerAnon(): Result<UserTokenJson> =
        call {
            client.get(EndPoint.RegisterAnonymously())
        }

    override suspend fun register(user: UserJson): Result<UserTokenJson> =
        call {
            client.get(EndPoint.Register()) {
                setBody(user)
            }
        }

    override suspend fun login(user: UserJson): Result<UserTokenJson> =
        call {
            client.get(EndPoint.Login()) {
                setBody(user)
            }
        }

    override suspend fun forceRefresh(token: UserRefreshTokenJson): Result<UserTokenJson> =
        call {
            client.get(EndPoint.RefreshToken()) {
                setBody(token)
            }
        }
}
