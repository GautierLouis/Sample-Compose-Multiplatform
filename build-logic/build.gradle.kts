plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.13.0")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
    compileOnly("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.8.2")
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatformApplication") {
            id = "com.louisgautier.application.convention"
            implementationClass = "ApplicationPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = "com.louisgautier.library.convention"
            implementationClass = "LibraryPlugin"
        }
    }
}
