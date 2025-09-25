rootProject.name = "Sample"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

//App
include(":client:composeApp")
//Feature
include(":client:feature:biometric")
include(":client:feature:login")
include(":client:feature:gallery")
//Domain
include(":client:domain")
//Data
include(":client:data:network")
include(":client:data:database")
include(":client:data:platform")
include(":client:data:preferences")
//Server
include(":server")
//Shared (client - server)
include(":api-contracts")
