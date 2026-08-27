import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.linklibrary.utils"
        compileSdk = 37
    }

    jvm("desktop")

    listOf(
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
                // Desktop uses custom ViewModel implementation instead of AndroidX Lifecycle
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

// Note: AGP 9 uses android {} inside kotlin {} block for shared modules
// Traditional android {} block only used in separate Android app entry point modules

