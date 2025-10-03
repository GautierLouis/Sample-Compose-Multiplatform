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
            //TODO(Fix) : firebaseSdkPath point to a DerivedData on my computer : Move the SPM into this repo
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
}
