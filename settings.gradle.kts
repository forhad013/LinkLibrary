pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/androidx-compose/maven")
    }
}

rootProject.name = "LinkLibrary"
include(":app:androidApp")
include(":app:iosApp")
include(":app")
include(":database")
//include(":konsistTest")