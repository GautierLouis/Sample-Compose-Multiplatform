plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.utils)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
