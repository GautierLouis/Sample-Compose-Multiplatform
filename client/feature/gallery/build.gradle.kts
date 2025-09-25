plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.client.core.permission)
            implementation(projects.client.core.logger)
        }
    }
}