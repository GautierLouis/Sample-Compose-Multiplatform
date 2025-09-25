plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.client.data.network)
                api(projects.client.data.database)
                api(projects.client.data.preferences)
            }
        }
    }
}
