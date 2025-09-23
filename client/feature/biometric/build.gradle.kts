plugins {
    id("com.louisgautier.library.convention")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.data.platform)
            implementation(libs.androidx.biometeric)
        }
    }
}