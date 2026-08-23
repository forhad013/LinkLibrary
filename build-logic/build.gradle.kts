plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:2.1.0")
    implementation("com.android.library:com.android.library.gradle.plugin:8.9.1")
    implementation("com.android.application:com.android.application.gradle.plugin:8.9.1")
    implementation("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.9.3")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:2.1.0")
    implementation("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:2.1.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.0-1.0.29")
    implementation("androidx.room:androidx.room.gradle.plugin:2.8.3")
}