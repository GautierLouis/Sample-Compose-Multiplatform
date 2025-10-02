import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class ConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.plugins.kotlin.multiplatform.get().pluginId)
            apply(libs.plugins.serialization.get().pluginId)
            apply(libs.plugins.mokkery.plugin.get().pluginId)
        }

        val type = extensions.findByType<ApplicationExtension>()
            ?: extensions.findByType<LibraryExtension>()
            ?: error("At least one of Application or Library must be set")

        type.applySharedAndroidSettings(this)

        extensions.configure<KotlinMultiplatformExtension> {
            configureTargets(this)
            configureBaseSourceSets(this)
        }

    }

    private fun configureTargets(
        extension: KotlinMultiplatformExtension
    ) = extension.apply {
        jvmToolchain(17)

        androidTarget()


        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            sourceSets.findByName(iosTarget.name)
                ?.kotlin?.srcDir("build/generated/ksp/$iosTarget/kotlin")

            iosTarget.binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }

        jvm()

        macosX64()
        macosArm64()

        applyDefaultHierarchyTemplate()
    }

    private fun Project.configureBaseSourceSets(
        extension: KotlinMultiplatformExtension
    ) = extension.apply {
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
                implementation(libs.kotlinx.coroutines.test)
            }

            androidMain.dependencies {
                implementation(libs.koin.android)
            }
        }
    }

    private fun CommonExtension<*, *, *, *, *, *>.applySharedAndroidSettings(project: Project) {
        namespace = "com.louisgautier.${project.nameFormatted}"
        compileSdk = project.libs.versions.android.compile.sdk.get().toInt()
        defaultConfig {
            minSdk = project.libs.versions.android.min.sdk.get().toInt()
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}

