import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the Kotlin Multiplatform plugin for the project.
 *
 * This function applies common configurations for a Kotlin Multiplatform project, including:
 * - Setting the JVM toolchain version.
 * - Configuring the Android target with specific compiler options.
 * - Setting up iOS targets (X64, Arm64, SimulatorArm64) and their framework configurations.
 * - Adding a JVM target.
 * - Applying the default source set hierarchy template.
 * - Adding common dependencies to the `commonMain`, `commonTest`, and `androidMain` source sets.
 *
 * @param extension The [KotlinMultiplatformExtension] to configure.
 */
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {

    jvmToolchain(17)

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp$nameFormatted"
            isStatic = true
        }
    }

    jvm()

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain.dependencies {
            implementation(conventionLibs.findLibrary("koin-core").get())
            implementation(conventionLibs.findLibrary("kotlinx-datetime").get())
            implementation(conventionLibs.findLibrary("kotlinx-coroutines-core").get())
        }

        commonTest.dependencies {
            implementation(conventionLibs.findLibrary("kotlin-test").get())
            implementation(conventionLibs.findLibrary("koin-test").get())
        }

        androidMain.dependencies {
            implementation(conventionLibs.findLibrary("koin-android").get())
        }
    }
}
