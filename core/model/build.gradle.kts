import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

// Domain models and repository interfaces: no Room, no Compose, so every target (incl. web) can use them.
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.lordofthelinks.model"
        compileSdk = 37
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core) // Flow in the repository interfaces
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
