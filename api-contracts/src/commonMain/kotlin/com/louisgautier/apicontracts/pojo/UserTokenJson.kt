package com.louisgautier.apicontracts.pojo

import kotlinx.serialization.Serializable

@Serializable
data class UserTokenJson(val token: String, val refreshToken: String)