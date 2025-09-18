package com.louisgautier.sample.server

import com.louisgautier.sample.server.domain.NoteRepository
import com.louisgautier.sample.server.domain.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.install
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

enum class Environment {
    PROD, DEV, TEST
}

fun Application.installInjection() {
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        modules(
            module {
                single<ApplicationEnvironment> { environment }
                single<Environment> {
                    val prop = environment.config.propertyOrNull("ktor.deployment.environment")
                        ?.getString()
                    when (prop) {
                        "dev" -> Environment.DEV
                        "test" -> Environment.TEST
                        else -> Environment.PROD
                    }
                }
            },
            serverModule,
            databaseModule,
            domainModule,
        )
    }
}

val serverModule = module {
    single {
        with(get<ApplicationEnvironment>().config) {
            JwtConfig(
                property("ktor.jwt.realm").getString(),
                property("ktor.jwt.secret").getString(),
                property("ktor.jwt.issuer").getString(),
                property("ktor.jwt.audience").getString()
            )
        }
    }

    single { JwtBuilder(get()) }
    single { PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
}

val databaseModule = module {
    single<DatabaseConfig> {
        with(get<ApplicationEnvironment>().config) {
            DatabaseConfig(
                property("datasource.url").getString(),
                property("datasource.user").getString(),
                property("datasource.password").getString()
            )
        }
    }
}

val domainModule = module {
    single { UserRepository() }
    single { NoteRepository() }
}
