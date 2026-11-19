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
                implementation("org.jetbrains.compose.runtime:runtime:1.12.0")
                implementation("org.jetbrains.compose.foundation:foundation:1.12.0")
                implementation("org.jetbrains.compose.material3:material3:1.9.0-alpha04")
                implementation("org.jetbrains.compose.ui:ui:1.12.0")
                implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
                implementation("org.jetbrains.compose.components:components-resources:1.12.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.6.1")
                // Coil for image loading (Android only)
                implementation(libs.coil.compose)
                // Compose UI Tooling Preview for @Preview functions
                implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.greenrobotdev.lordofthelinks.design.resources"
}
