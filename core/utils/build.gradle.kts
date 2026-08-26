import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget {
        // AGP 9: Android configuration moved to androidTarget
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }

        // Android library configuration (AGP 9 style)
        // These configurations are now part of androidTarget instead of separate android {} block
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "utils"
            isStatic = true
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

                // KotlinX Coroutines
                implementation(libs.kotlinx.coroutines.core)

                // Note: Molecule runtime is platform-specific and added to androidMain, desktopMain, iosMain
                // It is not available for WASM
            }
        }

        val androidMain by getting {
            dependencies {
                // AndroidX Lifecycle ViewModel for Android
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
                // Molecule Runtime for Android
                implementation(libs.molecule.runtime)
            }
        }

        val desktopMain by getting {
            dependencies {
                // AndroidX Lifecycle ViewModel for Desktop
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
                // Molecule Runtime for Desktop
                implementation(libs.molecule.runtime)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                // Molecule Runtime for iOS
                implementation(libs.molecule.runtime)
            }
        }

        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

        val wasmJsMain by getting {
            dependencies {
                // Note: Molecule runtime not available for WASM
                // WASM targets cannot use MoleculeViewModel
            }
        }
    }
}

