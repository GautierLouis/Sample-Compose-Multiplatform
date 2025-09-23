plugins {
    id("com.louisgautier.library.convention")
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
