plugins {
    alias(libs.plugins.library.convention)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.client.core.utils)
            api(projects.client.core.logger)
            api(projects.client.core.permission)
        }
    }
}