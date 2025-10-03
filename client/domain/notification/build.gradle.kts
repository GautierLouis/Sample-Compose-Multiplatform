plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.compilations["main"].cinterops.create("firebase") {
            definitionFile = file("src/nativeInterop/cinterop/firebase.def")
        }
    }

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