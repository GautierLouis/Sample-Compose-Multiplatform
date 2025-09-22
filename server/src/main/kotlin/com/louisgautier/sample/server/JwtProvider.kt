package com.louisgautier.sample.server

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.net.URI
import java.security.interfaces.ECPublicKey
import java.util.concurrent.TimeUnit

class JwtProvider(
    private val buildEnvironment: BuildEnvironment
) {

    private val provider by lazy {
        JwkProviderBuilder(URI(buildEnvironment.supabaseJwks).toURL())
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }

    fun verify(httpAuthHeader: HttpAuthHeader): JWTVerifier? {
        return try {

            val token = (httpAuthHeader as? HttpAuthHeader.Single)?.blob ?: return null

            val publicKey = provider.get(JWT.decode(token).keyId).publicKey

            if (publicKey !is ECPublicKey) {
                return null
            }

            return JWT.require(Algorithm.ECDSA256(publicKey, null))
                .withIssuer(buildEnvironment.supabaseIssuer)
                .withAudience(buildEnvironment.supabaseAudience)
                .build()

        } catch (_: Exception) {
            null
        }
    }

    fun validate(credential: JWTCredential): JWTPrincipal? {
        return if (credential.payload.getClaim(Constants.CLAIM).asString() != null) {
            JWTPrincipal(credential.payload)
        } else null
    }
}