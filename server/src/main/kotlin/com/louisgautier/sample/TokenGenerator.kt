package com.louisgautier.sample

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.louisgautier.shared.routing.UserToken
import java.time.Instant
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

@OptIn(ExperimentalTime::class)
fun generateTokenFor(
    username: String,
    secret: String,
    issuer: String,
    audience: String,
): UserToken {

    val now = Clock.System.now().toJavaInstant()
    val `3h`: Long = 10800
    val `48h`: Long = 172800

    val token = generateToken(
        secret,
        issuer,
        audience,
        username,
        now.plusSeconds(`3h`)
    )

    val refresh = generateToken(
        secret,
        issuer,
        audience,
        username,
        now.plusSeconds(`48h`)
    )

    return UserToken(token, refresh)
}

fun generateToken(
    secret: String,
    issuer: String,
    audience: String,
    username: String,
    duration: Instant,
): String {

    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", username)
        .withExpiresAt(duration)
        .sign(Algorithm.HMAC256(secret))
}