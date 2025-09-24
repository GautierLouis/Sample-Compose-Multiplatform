plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.data.platform)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
