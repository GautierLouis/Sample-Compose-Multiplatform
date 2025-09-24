plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.data.network)
                implementation(projects.client.data.database)
                implementation(projects.client.data.preferences)
            }
        }
    }
}
