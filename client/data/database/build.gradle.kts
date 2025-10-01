plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
    alias(libs.plugins.androidx.room)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.client.core.logger)
            implementation(projects.client.core.utils)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }
    }
}

android {
    room {
        schemaDirectory("$projectDir/schemas")
    }
}
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
    add("kspMacosArm64", libs.androidx.room.compiler)
    add("kspMacosX64", libs.androidx.room.compiler)
}
