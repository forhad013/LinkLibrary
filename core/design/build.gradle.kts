import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.linklibrary.design"
        compileSdk = 37
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
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.6.1")
            }
        }
    }
}

