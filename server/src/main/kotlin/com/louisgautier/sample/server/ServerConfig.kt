package com.louisgautier.sample.server

import com.louisgautier.apicontracts.routing.defaultJson
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.ktor.ext.inject
import java.time.Duration


fun Application.configureServer() {

    val appMicrometerRegistry: PrometheusMeterRegistry by inject()
    val jwtProvider: JwtProvider by inject()

    install(ContentNegotiation) {
        json(defaultJson)
    }
    install(Resources)
    install(Authentication) {
        jwt(Constants.JWT_NAME) {

            verifier { header -> jwtProvider.verify(header) }

            validate { credential -> jwtProvider.validate(credential) }

            challenge { _, _ ->
                call.respond(
                    status = io.ktor.http.HttpStatusCode.Unauthorized,
                    message = mapOf("error" to "Invalid or missing token")
                )
            }
        }
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .maximumExpectedValue(Duration.ofSeconds(20).toNanos().toDouble())
            .serviceLevelObjectives(
                Duration.ofMillis(100).toNanos().toDouble(),
                Duration.ofMillis(500).toNanos().toDouble()
            )
            .build()
    }
}