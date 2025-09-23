package com.louisgautier.network

import com.louisgautier.apicontracts.pojo.UserJson
import com.louisgautier.apicontracts.routing.Root
import com.louisgautier.network.interfaces.UserService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get

class DefaultUserService(
    private val client: HttpClient
) : UserService {
    override suspend fun me(): Result<UserJson> = call {
        client.get(Root.Me())
    }

}