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

//Server
include(":server")
//Shared (client - server)
include(":api-contracts")
//App
include(":client:composeApp")
//Feature
include(":client:feature:biometric")
include(":client:feature:login")
include(":client:feature:gallery")
//Domain
include(":client:domain")
include(":client:domain:auth")
//Data
include(":client:data")
include(":client:data:network")
include(":client:data:database")
include(":client:data:preferences")
//Core
include(":client:core")
include(":client:core:logger")
include(":client:core:utils")
include(":client:core:permission")
include(":client:core:firebase")
//Design-System
include(":client:design-system")