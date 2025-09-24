plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.data.platform)
            implementation(libs.androidx.biometeric)
        }
    }
}