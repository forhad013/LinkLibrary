plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
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

    @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose
                implementation(compose.runtime)

                // Molecule
                implementation(libs.molecule.runtime)

                // KotlinX Coroutines
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val androidMain by getting {
            dependencies {
                // AndroidX Lifecycle ViewModel for Android
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
            }
        }

        val desktopMain by getting {
            dependencies {
                // Lifecycle ViewModel for Desktop
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-desktop:1.0.0")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val wasmJsMain by getting {
            dependencies {
                // ViewModel stub/alternative for WASM
                // Note: Full AndroidX ViewModel not available for WASM yet
            }
        }
    }
}

android {
    namespace = "com.greenrobotdev.linklibrary.utils"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
