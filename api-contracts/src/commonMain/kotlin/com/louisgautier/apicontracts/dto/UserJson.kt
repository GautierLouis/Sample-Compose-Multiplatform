package com.louisgautier.apicontracts.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserJson(val id: Int, val username: String, val password: String)