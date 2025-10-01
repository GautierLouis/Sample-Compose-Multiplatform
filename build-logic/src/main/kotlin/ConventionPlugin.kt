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
            apply(libs.plugins.ksp.get().pluginId)
        }

        val type = extensions.findByType<ApplicationExtension>()
            ?: extensions.findByType<LibraryExtension>()
            ?: error("At least one of Application or Library must be set")

        type.applySharedAndroidSettings(this)

        extensions.configure<KotlinMultiplatformExtension> {
            configureTargets(this)
            configureBaseSourceSets(this)
            addKoinKspToTargets(this)
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
                implementation(libs.koin.annotation)

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

    private fun Project.addKoinKspToTargets(
        extension: KotlinMultiplatformExtension
    ) = extension.apply {
        // Only add deps after the KSP plugin is applied (so the configurations exist)
        plugins.withId(libs.plugins.ksp.get().pluginId) {

            val kspConfigs = listOf(
                "kspAndroid",
                "kspIosSimulatorArm64",
                "kspIosX64",
                "kspIosArm64",
                "kspJvm",
                "kspMacosArm64",
                "kspMacosX64"
            )

            kspConfigs.forEach { cfg ->
                // only add if the configuration exists in this project
                if (project.configurations.findByName(cfg) != null) {
                    project.dependencies.add(cfg, libs.koin.ksp)
                    project.logger.debug(
                        "KspRoomConventionPlugin: added {} to configuration '{}' in {}",
                        libs.koin.ksp,
                        cfg,
                        project.path
                    )
                } else {
                    project.logger.debug("KspRoomConventionPlugin: configuration '$cfg' not found in ${project.path}, skipping.")
                }
            }

            sourceSets.named("commonMain").configure {
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }

            tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }
                .configureEach {
                    dependsOn("kspCommonMainKotlinMetadata")
                }

            tasks.matching { it.name.startsWith("generateResourceAccessorsForAndroid") }
                .configureEach {
                    dependsOn("kspDebugKotlinAndroid")
                    mustRunAfter("kspDebugKotlinAndroid")
                }
        }
    }

}

