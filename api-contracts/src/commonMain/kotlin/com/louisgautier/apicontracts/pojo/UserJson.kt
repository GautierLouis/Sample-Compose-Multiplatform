package com.louisgautier.apicontracts.pojo

import kotlinx.serialization.Serializable

@Serializable
data class UserJson(val id: Int, val username: String, val password: String)