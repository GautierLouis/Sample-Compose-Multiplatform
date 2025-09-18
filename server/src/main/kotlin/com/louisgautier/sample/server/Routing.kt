package com.louisgautier.sample.server

// Removed: import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen
import com.louisgautier.apicontracts.Greeting
import com.louisgautier.apicontracts.pojo.UserJson
import com.louisgautier.apicontracts.pojo.UserTokenJson
import com.louisgautier.apicontracts.routing.Login
import com.louisgautier.apicontracts.routing.Notes
import com.louisgautier.apicontracts.routing.Protected
import com.louisgautier.apicontracts.routing.RefreshToken
import com.louisgautier.apicontracts.routing.Unprotected
import com.louisgautier.sample.server.domain.NoteRepository
import com.louisgautier.sample.server.domain.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val jwtBuilder: JwtBuilder by inject()
    val userRepository: UserRepository by inject()
    val noteRepository: NoteRepository by inject()
    val appMicrometerRegistry: PrometheusMeterRegistry by inject()

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

            val accessToken = jwtBuilder.makeAccessToken(storedUser!!)
            val refreshToken = jwtBuilder.makeRefreshToken(storedUser)

            call.respond(HttpStatusCode.OK, UserTokenJson(accessToken, refreshToken))

        }

        post<RefreshToken> {
            val oldToken = call.receive<UserTokenJson>()

            try {
                val decodedJWT =
                    jwtBuilder.verifier(JwtBuilder.TokenType.REFRESH).verify(oldToken.refreshToken)
                val userId = decodedJWT.getClaim("userId").asInt()
                val tokenType = decodedJWT.getClaim("type").asString()

                if (tokenType != "refresh" && userId == null) {
                    throw Exception()
                }

                val user = userRepository.getUserById(userId)

                if (user == null) {
                    throw Exception()
                }

                val newAccessToken = jwtBuilder.makeAccessToken(user)
                val newRefreshToken = jwtBuilder.makeRefreshToken(user)

                call.respond(HttpStatusCode.OK, UserTokenJson(newAccessToken, newRefreshToken))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid refresh token"))
            }
        }

        swaggerUI(path = "swagger")

        openAPI(path = "openapi")

        get("/metrics") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}