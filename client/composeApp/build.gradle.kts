import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.convention.plugin)
    alias(libs.plugins.compose.convention)
}

kotlin {
    sourceSets {

        commonMain.dependencies {
            implementation(projects.client.core)
            implementation(projects.client.feature.biometric)
            implementation(projects.client.feature.gallery)
            implementation(projects.client.feature.login)
            implementation(projects.client.designSystem)
            implementation(projects.client.domain) // needed for Koin
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }

        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
        }

        commonTest.dependencies {
            implementation(libs.junit)
        }
    }
}
android {
    defaultConfig {
        applicationId = "com.louisgautier.composeApp"
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = libs.versions.app.version.asProvider().get()
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.louisgautier.composeApp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.louisgautier.composeApp"
            packageVersion = libs.versions.app.version.asProvider().get()
        }
    }
}
