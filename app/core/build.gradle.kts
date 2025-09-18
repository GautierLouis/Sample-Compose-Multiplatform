import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mokkeryPluging)
}

kotlin {

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
            baseName = "ComposeAppCore"
            isStatic = true
        }
    }


    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.koin.core)
            api(libs.kotlinx.datetime)

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            implementation(libs.kermit)

            api(libs.kotlinx.coroutinesCore)
        }

        commonTest.dependencies {
            api(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.louisgautier.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}