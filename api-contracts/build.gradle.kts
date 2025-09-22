plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.resources)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}
