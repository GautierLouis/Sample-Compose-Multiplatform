package com.louisgautier.apicontracts.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserJson(
    val email: String,
    val password: String,
)
