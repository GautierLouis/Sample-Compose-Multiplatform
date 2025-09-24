import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project


// Helper function for shared Android settings (Application and Library)
internal fun CommonExtension<*, *, *, *, *, *>.applySharedAndroidSettings(project: Project) {
    namespace = "com.louisgautier.${project.nameFormatted}"
    compileSdk = project.libs.versions.android.compile.sdk.get().toInt()
    defaultConfig {
        minSdk = project.libs.versions.android.min.sdk.get().toInt()
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
