plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.platform)
                implementation(projects.app.network)
                implementation(projects.app.database)

                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
