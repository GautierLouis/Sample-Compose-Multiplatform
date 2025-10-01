plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.data)
            }
        }
    }
}