import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.lordofthelinks.design"
        compileSdk = 37
        androidResources { enable = true } // required for Compose resources on the KMP Android library plugin
    }

    jvm("desktop")

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "design"
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
                // Versions come from the Compose plugin, like the other modules
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(libs.jetbrains.compose.material3) // alpha, for the Expressive APIs
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
            }
        }

        val androidMain by getting {
            dependencies {
                // Coil for image loading (Android only)
                implementation(libs.coil.compose)
                // Compose UI Tooling Preview for @Preview functions
                implementation(libs.compose.ui.tooling.preview)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.greenrobotdev.lordofthelinks.design.resources"
}
