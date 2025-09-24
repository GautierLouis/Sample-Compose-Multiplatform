import org.gradle.api.Project

internal fun Project.configureBasePlugin() {
    with(pluginManager) {
        apply(libs.plugins.kotlin.multiplatform.get().pluginId)
        apply(libs.plugins.serialization.get().pluginId)
        apply(libs.plugins.mokkery.plugin.get().pluginId)
    }
}