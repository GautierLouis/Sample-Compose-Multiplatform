package com.louisgautier.sample.server

import com.louisgautier.sample.server.domain.AuthenticationRepository
import com.louisgautier.sample.server.domain.AuthenticationRepositoryImpl
import com.louisgautier.sample.server.domain.NoteRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.install
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.installInjection(envModule: Module) {
    install(Koin) {
        slf4jLogger(Level.INFO)
        modules(listOf(envModule) + standardModules)
    }
}

private val serverModule = module {
    single { PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }
    single { JwtProvider(get()) }
}

private val domainModule = module {
    single { AuthenticationRepositoryImpl(get()) } bind AuthenticationRepository::class
    single { NoteRepository() }
}

private val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = get<BuildEnvironment>().supabaseUrl,
            supabaseKey = get<BuildEnvironment>().supabasePublicKey
        ) {
            install(Auth)
        }
    }
}

fun buildEnvModule(applicationEnvironment: ApplicationEnvironment) = module {
    single {
        BuildEnvironment.build(applicationEnvironment)
    }
}

private val standardModules = listOf(serverModule, domainModule, supabaseModule)
