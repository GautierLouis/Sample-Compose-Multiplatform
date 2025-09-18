package com.louisgautier.sample.server

import com.louisgautier.sample.server.database.DatabaseConfig
import com.louisgautier.sample.server.domain.NoteRepository
import com.louisgautier.sample.server.domain.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.install
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

object Injection {
    val environment = named("environment")
}

fun Application.installInjection() {
    install(Koin) {
        slf4jLogger(Level.DEBUG)
        modules(
            serverModule,
            databaseModule,
            domainModule,
            module {
                single<ApplicationEnvironment> { environment }

                single(Injection.environment) {
                    System.getenv("APP_ENV") ?: environment.config.propertyOrNull("app.env")?.getString()
                }

                single<DatabaseConfig> {
                    val env = get<String>(Injection.environment)

                    val db = environment
                        .config.property("environments.${env}")
                        .getMap()["database"] as Map<String, String>

                    DatabaseConfig(
                        db["url"]!!,
                        db["driver"]!!,
                        db["user"]!!,
                        db["password"]!!
                    )
                }
            }
        )
    }
}

val serverModule = module {
    single { JwtConfig(get()) }
    single { PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
}

val databaseModule = module {

}

val domainModule = module {
    single { UserRepository() }
    single { NoteRepository() }
}
