plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.convention.plugin)
    alias(libs.plugins.compose.convention)
}

dependencies {
    debugImplementation(compose.uiTooling)
}
