package com.louisgautier.sample.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.louisgautier.sample.server.database.entity.StoredUser
import io.ktor.server.application.ApplicationEnvironment
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

class JwtConfig(
    environment: ApplicationEnvironment
) {

    enum class TokenType {
        ACCESS, REFRESH
    }

    val realm = environment.config.property("ktor.jwt.realm").getString()
    val secret = environment.config.property("ktor.jwt.secret").getString()
    val issuer = environment.config.property("ktor.jwt.issuer").getString()
    val audience = environment.config.property("ktor.jwt.audience").getString()
    private val algorithm = Algorithm.HMAC256(secret)

    @OptIn(ExperimentalTime::class)
    private val now = Clock.System.now().toJavaInstant()
    private val `3h`: Long = 10800
    private val `48h`: Long = 172800

    fun makeAccessToken(user: StoredUser): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", user.id)
        .withClaim("type", TokenType.ACCESS.name)
        .withClaim("time", now)
        .withExpiresAt(now.plusSeconds(`3h`))
        .sign(algorithm)

    fun makeRefreshToken(user: StoredUser): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userId", user.id)
        .withClaim("type", TokenType.REFRESH.name)
        .withClaim("time", now)
        .withExpiresAt(now.plusSeconds(`48h`))
        .sign(algorithm)

    fun verifier(type: TokenType): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("type", type.name)
        .build()

}
