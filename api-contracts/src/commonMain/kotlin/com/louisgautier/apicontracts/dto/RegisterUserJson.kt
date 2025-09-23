package com.louisgautier.apicontracts.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserJson(
    val email: String,
    val password: String
)
