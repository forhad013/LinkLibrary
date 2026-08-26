import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        // AGP 9: Android configuration moved to androidTarget
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xcontext-receivers")
        }

        // Android library configuration (AGP 9 style)
        // buildFeatures.compose = true and composeOptions handled differently in AGP 9
        // Compose integration now managed through kotlin plugin compose
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
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        val androidMain by getting {
            dependencies {
                // Database module (only for targets that support it)
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
                // Database module (only for targets that support it)
                implementation(project(":database"))
                // Ktor HTTP client engine for JVM
                implementation(libs.ktor.client.java)
                // Molecule for JVM
                implementation(libs.molecule.runtime)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
            }
        }
    }
}

// Note: -Xinline-optimizations flag removed as it's not supported in Kotlin 2.1.0
// Note: buildFeatures.compose and composeOptions handled differently in AGP 9
// Compose integration now managed through org.jetbrains.kotlin.plugin.compose