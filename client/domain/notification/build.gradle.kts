plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.core.logger)
            implementation(projects.client.core.utils)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.config)

            implementation(libs.kotlinx.coroutines.play.services)
        }
    }
}