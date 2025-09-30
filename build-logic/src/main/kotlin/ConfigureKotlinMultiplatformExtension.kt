import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the Kotlin Multiplatform plugin for the project.
 *
 * This function applies common configurations for a Kotlin Multiplatform project, including:
 * - Setting the JVM toolchain version.
 * - Configuring the Android target.
 * - Setting up iOS targets (X64, Arm64, SimulatorArm64) and their framework configurations.
 * - Adding a JVM target.
 * - Adding Macos targets (X64, Arm64).
 * - Applying the default source set hierarchy template.
 * - Adding common dependencies to the `commonMain`, `commonTest`, and `androidMain` source sets.
 *
 * @param extension The [KotlinMultiplatformExtension] to configure.
 */
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {

    jvmToolchain(17)

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    macosX64()
    macosArm64()

    applyDefaultHierarchyTemplate()
    sourceSets.apply {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.mokkery.coroutine)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
