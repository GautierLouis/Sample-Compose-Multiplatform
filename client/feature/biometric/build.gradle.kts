plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.core.utils)
            implementation(libs.androidx.biometeric)
        }
    }
}