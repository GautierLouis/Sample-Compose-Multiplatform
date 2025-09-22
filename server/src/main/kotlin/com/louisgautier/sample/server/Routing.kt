package com.louisgautier.sample.server

import com.louisgautier.apicontracts.Greeting
import com.louisgautier.apicontracts.pojo.RegisterUserJson
import com.louisgautier.apicontracts.pojo.UserRefreshTokenJson
import com.louisgautier.apicontracts.routing.Root
import com.louisgautier.sample.server.domain.AuthenticationRepository
import com.louisgautier.sample.server.domain.NoteRepository
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
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    routing {
        configureAdminRoutes()
        configureOpenRoutes()
        configureAuthedRoutes()

        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
}

private fun Routing.configureAuthedRoutes() {
    authenticate(Constants.JWT_NAME) {
        val noteRepository: NoteRepository by inject()
        val authenticationRepository: AuthenticationRepository by inject()

        get<Root.Me> {
            val principal = call.principal<JWTPrincipal>()!!
            val supabaseUserId = principal.payload.getClaim("sub").asString()
            call.respond(mapOf("user_id" to supabaseUserId))
        }

        post<Root.RefreshToken> {
            val creds = call.receive<UserRefreshTokenJson>()
            authenticationRepository.refreshSession(creds.refreshToken).collect {
                it.onSuccess { data ->
                    call.respond(HttpStatusCode.OK, data)
                }
                it.onFailure {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }

        get<Root.Notes> {
            val notes = noteRepository.getAllNotes(it.page ?: 0, it.limit ?: 100)
            call.respond(HttpStatusCode.OK, notes)
        }

        get<Root.Notes.Id> {
            val note = noteRepository.getNoteById(it.id)
            if (note == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, note)
            }
        }
    }
}

private fun Routing.configureOpenRoutes() {
    val authenticationRepository: AuthenticationRepository by inject()

    get<Root.RegisterAnonymously> {
        authenticationRepository.registerAnonymously().collect {
            it.onSuccess { data ->
                call.respond(HttpStatusCode.OK, data)
            }
            it.onFailure {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    post<Root.Register> {
        val creds = call.receive<RegisterUserJson>()

        authenticationRepository.registerWith(creds.email, creds.password).collect {
            it.onSuccess {
                call.respond(HttpStatusCode.OK, it)
            }
            it.onFailure {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }

    post<Root.Login> {
        val creds = call.receive<RegisterUserJson>()

        authenticationRepository.registerWith(creds.email, creds.password).collect {
            it.onSuccess {
                call.respond(HttpStatusCode.OK, it)
            }
            it.onFailure {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}

private fun Routing.configureAdminRoutes() {
    val appMicrometerRegistry: PrometheusMeterRegistry by inject()

    swaggerUI(path = "admin/swagger")
    openAPI(path = "admin/openapi")
    get<Root.Admin.Metrics> {
        call.respond(appMicrometerRegistry.scrape())
    }
}