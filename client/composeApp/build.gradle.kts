import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.application.convention)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(projects.client.feature.biometric)
            implementation(projects.client.domain)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)

            implementation(libs.compose.lifecycle.viewmodel.compose)
            implementation(libs.compose.lifecycle.runtime.compose)
            implementation(libs.compose.navigation.compose)

            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
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
