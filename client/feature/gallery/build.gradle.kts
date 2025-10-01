plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.client.core.permission)
            implementation(projects.client.core.utils)
            implementation(projects.client.core.logger)
        }
    }
}