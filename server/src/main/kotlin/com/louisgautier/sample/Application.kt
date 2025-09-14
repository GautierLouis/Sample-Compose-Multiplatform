package com.louisgautier.sample

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.louisgautier.shared.Greeting
import com.louisgautier.shared.routing.Login
import com.louisgautier.shared.routing.Logout
import com.louisgautier.shared.routing.Protected
import com.louisgautier.shared.routing.RefreshToken
import com.louisgautier.shared.routing.Unprotected
import com.louisgautier.shared.routing.User
import com.louisgautier.shared.routing.UserToken
import com.louisgautier.shared.routing.defaultJson
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.resources.Resources
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = EngineMain.main(args)


@OptIn(ExperimentalTime::class)
fun Application.module() {
    val myRealm = environment.config.property("ktor.jwt.realm").getString()
    val secret = environment.config.property("ktor.jwt.secret").getString()
    val issuer = environment.config.property("ktor.jwt.issuer").getString()
    val audience = environment.config.property("ktor.jwt.audience").getString()

    install(ContentNegotiation) {
        json(defaultJson)
    }
    install(Resources)
    install(CallLogging) {
        level = Level.INFO
    }

    install(Authentication) {
        jwt {
            realm = myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    val userRepository = UserRepository()

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        authenticate {
            get<Protected> {
                call.respondText("You are authenticated")
            }
        }
        get<Unprotected> {
            call.respondText("You are not authenticated")
        }

        post<Login> {
            val receivedUser = call.receive<User>()

            val storedUser = userRepository.getUser(receivedUser.username)

            if (storedUser == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }

            val newToken = generateTokenFor(
                username = storedUser!!.username,
                secret = secret,
                issuer = issuer,
                audience = audience,
            )

            val new = storedUser.copy(token = newToken.token, refreshToken = newToken.refreshToken)

            userRepository.updateUser(new)

            call.respond(newToken)
        }

        post<RefreshToken> {
            val oldToken = call.receive<UserToken>()

            val storedUser = userRepository.getUserByToken(oldToken.token)

            if (storedUser == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }

            val newToken = generateTokenFor(
                username = storedUser!!.username,
                secret = secret,
                issuer = issuer,
                audience = audience,
            )

            val new = storedUser.copy(token = newToken.token, refreshToken = newToken.refreshToken)

            userRepository.updateUser(new)

            call.respond(newToken)
        }

    }
}

internal class UserRepository() {

    //Make a real database
    private val users = mutableListOf(
        StoredUser("test", "test", null, null)
    )

    fun getUser(username: String): StoredUser? {
        return users.firstOrNull { it.username == username }
    }

    fun getUserByToken(token: String): StoredUser? {
        return users.firstOrNull { it.token == token }
    }

    fun updateUser(user: StoredUser) {
        users.remove(users.first { it.username == user.username })
        users.add(user)
    }

    fun deleteUser(username: String) {
        users.remove(users.first { it.username == username })
    }

    fun disconnectUser(username: String) {
        val user = users.first { it.username == username }
        updateUser(user.copy(token = null, refreshToken = null))
    }

}


data class StoredUser(
    val username: String,
    val password: String,
    val token: String?,
    val refreshToken: String?
)



