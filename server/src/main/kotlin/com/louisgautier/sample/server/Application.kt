package com.louisgautier.sample.server

import com.louisgautier.apicontracts.Greeting
import com.louisgautier.apicontracts.routing.Login
import com.louisgautier.apicontracts.routing.Protected
import com.louisgautier.apicontracts.routing.RefreshToken
import com.louisgautier.apicontracts.routing.Unprotected
import com.louisgautier.apicontracts.pojo.UserJson
import com.louisgautier.apicontracts.pojo.UserTokenJson
import com.louisgautier.apicontracts.routing.Notes
import com.louisgautier.apicontracts.routing.defaultJson
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
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

    val jwtConfig = JwtConfig(this.environment)
    val userRepository = UserRepository()
    val noteRepository = NoteRepository()

    install(ContentNegotiation) {
        json(defaultJson)
    }
    install(Resources)
    install(CallLogging) {
        level = Level.INFO
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwtConfig.verifier(JwtConfig.TokenType.ACCESS))
            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else null
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        authenticate("auth-jwt") {
            get<Protected> {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()

                call.respond(HttpStatusCode.OK, "Hello, authenticated user with id $userId!")
            }

            get<Notes> {
                val notes = noteRepository.getAllNotes(it.page ?: 0, it.limit ?: 100)
                call.respond(HttpStatusCode.OK, notes)
            }

            get<Notes.Id> {
                val note = noteRepository.getNoteById(it.id)
                if (note == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(HttpStatusCode.OK, note)
                }
            }
        }

        get<Unprotected> {
            call.respondText("You are not authenticated")
        }

        post<Login> {
            val receivedUser = call.receive<UserJson>()
            val storedUser = userRepository.getUserById(receivedUser.id)

            if (storedUser == null || receivedUser.password != storedUser.password) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
            }

            val accessToken = jwtConfig.makeAccessToken(storedUser!!)
            val refreshToken = jwtConfig.makeRefreshToken(storedUser)

            call.respond(HttpStatusCode.OK, UserTokenJson(accessToken, refreshToken))

        }

        post<RefreshToken> {
            val oldToken = call.receive<UserTokenJson>()

            try {
                val decodedJWT =
                    jwtConfig.verifier(JwtConfig.TokenType.REFRESH).verify(oldToken.refreshToken)
                val userId = decodedJWT.getClaim("userId").asInt()
                val tokenType = decodedJWT.getClaim("type").asString()

                if (tokenType != "refresh" && userId == null) {
                    throw Exception()
                }

                val user = userRepository.getUserById(userId)

                if (user == null) {
                    throw Exception()
                }

                val newAccessToken = jwtConfig.makeAccessToken(user)
                val newRefreshToken = jwtConfig.makeRefreshToken(user)

                call.respond(HttpStatusCode.OK, UserTokenJson(newAccessToken, newRefreshToken))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid refresh token"))
            }
        }
    }
}



