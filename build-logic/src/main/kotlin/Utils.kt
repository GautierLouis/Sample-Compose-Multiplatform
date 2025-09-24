import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.the

internal val Project.conventionLibs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

val Project.nameFormatted: String
    get() = name.replace("-", "")
