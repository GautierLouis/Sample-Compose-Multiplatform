plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.client.core.utils)
            api(projects.client.core.logger)
            api(projects.client.core.permission)
            api(projects.client.core.firebase)
        }
    }
}