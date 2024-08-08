enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://s3.amazonaws.com/mirego-maven/public")
    }
}

rootProject.name = "SwissBorgMobileTechChallenge"
include(":androidApp")
include(":shared")
