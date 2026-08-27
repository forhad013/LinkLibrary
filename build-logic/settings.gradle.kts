dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        google()          // Required for Android/AGP artifacts
        mavenCentral()
    }

    // Enable version catalog access for convention plugins
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}