package com.louisgautier.shared.routing

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
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




@Serializable
data class User(val username: String, val password: String)

@Serializable
data class UserToken(val token: String, val refreshToken: String)

