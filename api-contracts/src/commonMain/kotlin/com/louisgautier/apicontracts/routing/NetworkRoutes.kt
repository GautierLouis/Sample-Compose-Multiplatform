package com.louisgautier.apicontracts.routing

import io.ktor.resources.Resource
import kotlinx.serialization.json.Json

val defaultJson = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@Resource("/unprotected")
class Unprotected()

@Resource("/protected")
class Protected()

@Resource("/login")
class Login()

@Resource("/logout")
class Logout()

@Resource("/refresh_token")
class RefreshToken()

@Resource("/notes")
class Notes(val page: Int? = null, val limit: Int? = null) {
    @Resource("{id}")
    class Id(val parent: Notes = Notes(), val id: Int)
}
