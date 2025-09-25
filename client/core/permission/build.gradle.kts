plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.client.core.logger)
            implementation(projects.client.core.utils)
        }
    }
}