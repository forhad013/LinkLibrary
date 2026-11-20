import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.lordofthelinks.bookmarks"
        compileSdk = 37
    }

    jvm()

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
                implementation(libs.multiplatform.settings) // FeedFilterStore

                // Domain models and repository interfaces (the Room implementations live in :database, wired by the app)
                implementation(project(":core:model"))
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            // In-memory Settings (MapSettings) for FeedFilterStoreTest
            implementation(libs.multiplatform.settings.test)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        val androidMain by getting {
            dependencies {
                // Ktor HTTP client engine for Android
                implementation(libs.ktor.client.okhttp)
                // Molecule for Android
                implementation(libs.molecule.runtime)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
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

// AGP 9 configuration handled by kmp-library-convention plugin
