plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "4.3.1"
}

dependencies {
    implementation(libs.plugins.kotlinMultiplatform.get().toString())
    implementation(libs.plugins.androidLibrary.get().toString())
    implementation(libs.plugins.androidApplication.get().toString())
    implementation(libs.plugins.jetbrainsCompose.get().toString())
    implementation(libs.plugins.composeCompiler.get().toString())
    implementation(libs.plugins.kotlinSerialization.get().toString())
    implementation(libs.plugins.ksp.get().toString())
    implementation(libs.plugins.room.get().toString())
}