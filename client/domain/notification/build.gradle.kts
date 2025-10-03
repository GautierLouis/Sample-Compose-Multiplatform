import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
}

kotlin {
    val localProperties = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "firebase.properties")))
    }
    val firebaseSdkPath: String = localProperties.getProperty("FIREBASE_SDK_PATH").toString()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {

        it.compilations["main"].cinterops.create("firebase") {
            definitionFile = file("src/nativeInterop/cinterop/firebase.def")
            compilerOpts(
                "-I${firebaseSdkPath}/FirebaseRemoteConfig/Sources/Public",
                "-I${firebaseSdkPath}/FirebaseRemoteConfig/Sources/Public/FirebaseRemoteConfig",
                "-I${firebaseSdkPath}/FirebaseCore/Sources/Public",
                "-I${firebaseSdkPath}/FirebaseCore/Sources/Public/FirebaseCore"
            )
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(projects.client.core.logger)
            implementation(projects.client.core.utils)

            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.config)

            implementation(libs.kotlinx.coroutines.play.services)
        }
    }

//    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
//        compilations["main"].cinterops {
//            val firebase by creating {
//                definitionFile.set(project.file("src/nativeInterop/cinterop/firebase.def"))
//
//                val firebaseSdkPath: String = project.findProperty("FIREBASE_SDK_PATH").toString()
//                    //?: error("FIREBASE_SDK_PATH is not defined in gradle.properties")
//
//                compilerOpts(
//                    "-I${firebaseSdkPath}/FirebaseRemoteConfig/Sources/Public",
//                    "-I${firebaseSdkPath}/FirebaseRemoteConfig/Sources/Public/FirebaseRemoteConfig",
//                    "-I${firebaseSdkPath}/FirebaseCore/Sources/Public",
//                    "-I${firebaseSdkPath}/FirebaseCore/Sources/Public/FirebaseCore"
//                )
//            }
//        }
//    }
}
