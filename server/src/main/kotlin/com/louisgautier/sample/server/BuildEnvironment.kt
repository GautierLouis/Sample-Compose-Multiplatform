package com.louisgautier.sample.server

import io.ktor.server.application.ApplicationEnvironment

data class BuildEnvironment(
    val type: BuildEnvironmentType,
    val port: Int,
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    val supabaseUrl: String,
    val supabasePublicKey: String,
    val supabasePrivateKey: String,
    val supabaseJwtKey: String,
    val supabaseIssuer: String,
    val supabaseJwks: String,
    val supabaseAudience: String,
) {
    enum class BuildEnvironmentType {
        DEV, PROD
    }

    companion object Companion {
        fun build(environment: ApplicationEnvironment) = with(environment.config) {
            BuildEnvironment(
                type = BuildEnvironmentType.valueOf(property("app.env").getString()),
                port = property("ktor.deployment.port").getString().toInt(),
                databaseUrl = property("database.url").getString(),
                databaseUser = property("database.user").getString(),
                databasePassword = property("database.password").getString(),
                supabaseUrl = property("supabase.url").getString(),
                supabasePublicKey = property("supabase.publicKey").getString(),
                supabasePrivateKey = property("supabase.privateKey").getString(),
                supabaseJwtKey = property("supabase.jwtKey").getString(),
                supabaseIssuer = property("supabase.issuer").getString(),
                supabaseJwks = property("supabase.jwks").getString(),
                supabaseAudience = property("supabase.audience").getString(),
            )
        }
    }
}