import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }

    jvm() {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)

                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kotlinx.serialization.json)

                // Kotlinx DateTime
                implementation(libs.kotlinx.datetime)

                // Navigation 3
                implementation(libs.navigation3.ui)
                implementation(libs.lifecycle.viewmodel.navigation3)

                // Design module (Material 3 theme and components)
                implementation(project(":core:design"))

                // Utils module (MoleculeViewModel and utilities)
                implementation(project(":core:utils"))

                // Bookmarks module (shared models and screens)
                implementation(project(":bookmarks"))
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        val androidMain by getting {
            dependencies {
                // Database module (only for Android and JVM targets)
                implementation(project(":database"))
                // Ktor HTTP client engine for Android
                implementation(libs.ktor.client.okhttp)
                // Molecule for Android
                implementation(libs.molecule.runtime)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // Database module (only for Android and JVM targets)
                implementation(project(":database"))
                // Ktor HTTP client engine for JVM
                implementation(libs.ktor.client.java)
                // Molecule for JVM
                implementation(libs.molecule.runtime)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                // Note: Some dependencies from commonMain may not be available for WASM
                // :core:utils and :bookmarks are excluded here due to ViewModel/Database dependencies
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)

                // Basic Ktor and utilities that work with WASM
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
            }
        }

        val wasmJsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }

}

android {
    namespace = "com.greenrobotdev.linklibrary"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

// Note: -Xinline-optimizations flag removed as it's not supported in Kotlin 2.1.0

// Enable Compose compiler metrics and performance monitoring
composeCompiler {
    metricsDestination = layout.buildDirectory.dir("compose-compiler-reports")
    reportsDestination = layout.buildDirectory.dir("compose-compiler-reports")
}
