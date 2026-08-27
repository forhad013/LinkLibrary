
plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.androidKotlinMultiplatformLibrary).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.androidx.room).apply(false)
    alias(libs.plugins.ksp).apply(false)

}
repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}
