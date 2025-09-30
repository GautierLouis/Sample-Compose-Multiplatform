import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
    compileOnly(libs.android.gradle.get())
    compileOnly(libs.kotlin.gradle.get())
    implementation(libs.compose.gradle.get())
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatformApplication") {
            id = libs.plugins.application.convention.get().pluginId
            implementationClass = "ApplicationPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = libs.plugins.library.convention.get().pluginId
            implementationClass = "LibraryPlugin"
        }
        register("kotlinMultiplatformCompose") {
            id = libs.plugins.compose.convention.get().pluginId
            implementationClass = "ComposePlugin"
        }
    }
}
