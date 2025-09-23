import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.conventionLibs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.nameFormatted: String
    get() = name.replace("-", "")

internal fun Project.installBasePlugin() {
    with(pluginManager) {
        apply(project.conventionLibs.findPlugin("kotlin-multiplatform").get().get().pluginId)
        apply(project.conventionLibs.findPlugin("serialization").get().get().pluginId)
        apply(project.conventionLibs.findPlugin("mokkery-plugin").get().get().pluginId)
        apply(project.conventionLibs.findPlugin("klint").get().get().pluginId)
    }
}


// Helper function for shared Android settings (Application and Library)
internal fun CommonExtension<*, *, *, *, *, *>.applySharedAndroidSettings(project: Project) {
    namespace = "com.louisgautier.${project.nameFormatted}"
    compileSdk =
        project.conventionLibs.findVersion("android-compile-sdk").get().requiredVersion.toInt()
    defaultConfig {
        minSdk = project.conventionLibs.findVersion("android-min-sdk").get().requiredVersion.toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
