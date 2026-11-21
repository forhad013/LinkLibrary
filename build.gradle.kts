
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
    // Code coverage: the root report merges the modules listed in dependencies { kover(...) }
    alias(libs.plugins.kover)
    // Static analysis over every module from here: ./gradlew detekt
    alias(libs.plugins.detekt)

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

// Coverage of every module with tests; ./gradlew koverHtmlReport / koverXmlReport (build/reports/kover)
dependencies {
    kover(project(":app"))
    kover(project(":bookmarks"))
    kover(project(":database"))
}

// One detekt run over all modules' Kotlin sources (no type resolution, so no per-module setup).
// Findings already in the code live in detekt-baseline.xml; only new ones fail the build.
detekt {
    source.setFrom(
        fileTree(rootDir) {
            include("**/src/**/*.kt")
            exclude("**/build/**", ".claude/**", "build-logic/**", "konsistTest/**")
        }
    )
    config.setFrom("detekt.yml")
    buildUponDefaultConfig = true
    baseline = file("detekt-baseline.xml")
}
