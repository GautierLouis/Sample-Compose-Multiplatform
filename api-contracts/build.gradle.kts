plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}
