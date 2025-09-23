import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class ApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            installBasePlugin()
            apply(project.conventionLibs.findPlugin("android-application").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)

        extensions.configure<ApplicationExtension> {
            applySharedAndroidSettings(target)
        }

    }
}