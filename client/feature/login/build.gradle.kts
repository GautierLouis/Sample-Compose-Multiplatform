plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.client.domain)
        }
    }
}