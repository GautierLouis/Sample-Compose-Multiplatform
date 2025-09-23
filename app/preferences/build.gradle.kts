plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.platform)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
