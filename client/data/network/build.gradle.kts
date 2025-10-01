plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.logger)
                implementation(projects.client.core.utils)
                api(projects.apiContracts)

                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.resources)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }

        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
