package com.louisgautier.apicontracts.pojo

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserJson(
    val email: String,
    val password: String
)
