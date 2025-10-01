plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
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
