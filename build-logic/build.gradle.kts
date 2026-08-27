plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()          // Required for Android/AGP artifacts
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.7.1")
    implementation("com.android.tools.build:gradle:9.1.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.3.11")
    implementation("androidx.room:androidx.room.gradle.plugin:2.8.4")
}

// Register convention plugins for Gradle to discover
gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "convention.kmp-library"
            implementationClass = "convention.KmpLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "convention.android-room"
            implementationClass = "convention.AndroidRoomConventionPlugin"
        }
    }
}