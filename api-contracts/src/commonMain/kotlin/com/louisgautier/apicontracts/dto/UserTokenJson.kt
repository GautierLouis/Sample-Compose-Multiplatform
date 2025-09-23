package com.louisgautier.apicontracts.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserTokenJson(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
)

@Serializable
data class UserRefreshTokenJson(
    @SerialName("refresh_token")
    val refreshToken: String,
)


@Serializable
data class UserInfoJson(
    val id: String,
    val provider: List<UserAuthMethodProvider>,
)

@Serializable
enum class UserAuthMethodProvider {
    ANONYMOUS, EMAIL
}

@Serializable
data class UserRegistrationResponseJson(
    val user: UserInfoJson,
    val session: UserTokenJson,
)