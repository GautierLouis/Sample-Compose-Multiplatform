package com.louisgautier.sample.server


data class JwtConfig(
    val realm: String,
    val secret: String,
    val issuer: String,
    val audience: String,
)

data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
    val poolSize: Int,
    val sslMode: String,
)