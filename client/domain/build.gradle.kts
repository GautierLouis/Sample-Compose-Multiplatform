plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.domain.auth)
                implementation(projects.client.domain.notification)
                implementation(projects.client.data)
            }
        }
    }
}
