package com.louisgautier.sample.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.louisgautier.sample.server.database.entity.StoredUser
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

class JwtBuilder(
    val config: JwtConfig
) {

    enum class TokenType {
        ACCESS, REFRESH
    }

    private val algorithm = Algorithm.HMAC256(config.secret)

    @OptIn(ExperimentalTime::class)
    private val now = Clock.System.now().toJavaInstant()
    private val `3h`: Long = 10800
    private val `48h`: Long = 172800

    fun makeAccessToken(user: StoredUser): String = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("userId", user.id)
        .withClaim("type", TokenType.ACCESS.name)
        .withClaim("time", now)
        .withExpiresAt(now.plusSeconds(`3h`))
        .sign(algorithm)

    fun makeRefreshToken(user: StoredUser): String = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("userId", user.id)
        .withClaim("type", TokenType.REFRESH.name)
        .withClaim("time", now)
        .withExpiresAt(now.plusSeconds(`48h`))
        .sign(algorithm)

    fun verifier(type: TokenType): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("type", type.name)
        .build()

}
