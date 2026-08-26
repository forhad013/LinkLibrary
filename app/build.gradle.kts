import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

kotlin {
    androidLibrary {
        // AGP 9: Android library configuration
        namespace = "com.greenrobotdev.linklibrary.app"
        compileSdk = 36

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }

    jvm() {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
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
                // WASM testing uses different approach in Kotlin 2.3+
                // kotlin-test-js is no longer needed, dependencies inherited from commonTest
            }
        }
    }
}

// Note: AGP 9 uses androidLibrary {} inside kotlin {} block for shared modules
// Traditional android {} block only used in separate Android app entry point modules
// Note: buildFeatures.compose and composeOptions handled differently in AGP 9
// Compose integration now managed through org.jetbrains.kotlin.plugin.compose
