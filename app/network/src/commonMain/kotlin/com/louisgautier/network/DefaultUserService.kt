package com.louisgautier.network

import com.louisgautier.apicontracts.dto.UserJson
import com.louisgautier.apicontracts.routing.EndPoint
import com.louisgautier.network.interfaces.UserService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get

internal class DefaultUserService(
    private val client: HttpClient,
) : UserService {
    override suspend fun me(): Result<UserJson> =
        call {
            client.get(EndPoint.Me())
        }
}
