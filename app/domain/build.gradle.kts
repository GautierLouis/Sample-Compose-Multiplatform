plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.network)
                implementation(projects.app.database)
                implementation(projects.app.core)
            }
        }
    }
}
