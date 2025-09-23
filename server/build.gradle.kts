plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    application
}

group = "com.louisgautier.sample.server"
version = "1.0.0"

application {
    mainClass.set("com.louisgautier.sample.server.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sourceSets {
    main {
        resources.srcDir("src/main/resources")
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks {
    // produce a fat jar without classifier so Docker picks it up easily
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("app")
        archiveClassifier.set("")  // no "-all" suffix
        archiveVersion.set("")     // optional: remove version in name
    }
}

dependencies {
    implementation(projects.apiContracts)
    implementation(libs.kotlinx.datetime)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auto.head.response)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.default.header)
    implementation(libs.ktor.server.data.conversion)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.client.cio)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)
    implementation(libs.ehcache)
    implementation(libs.firebase.auth)

    implementation(project.dependencies.platform(libs.supabase.bom))
    implementation(libs.supabase.auth)

    implementation(libs.micrometer.registery.prometheus)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.koin.test)
    testImplementation("io.mockk:mockk:1.14.2")
    testImplementation("org.testcontainers:postgresql:1.19.0") // optional, integration
    testImplementation("com.h2database:h2:2.2.222")
}