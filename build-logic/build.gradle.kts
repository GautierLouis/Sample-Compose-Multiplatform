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
        register("kotlinMultiplatformCompose") {
            id = libs.plugins.compose.convention.get().pluginId
            implementationClass = "ComposePlugin"
        }

        register("kotlinMultiplatformConvention") {
            id = libs.plugins.convention.plugin.get().pluginId
            implementationClass = "ConventionPlugin"
        }
    }
}
