plugins {
    alias(libs.plugins.library.convention)
    alias(libs.plugins.compose.convention)
}

dependencies {
    debugImplementation(compose.uiTooling)
}
