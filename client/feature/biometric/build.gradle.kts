plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.core.utils)
            implementation(libs.androidx.biometeric)
        }
    }
}