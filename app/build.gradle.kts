plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    jvm() // Enable JVM target for desktop app

    // iOS targets removed - focusing on Android and Desktop for now
    // listOf(
    //     iosX64(),
    //     iosArm64(),
    //     iosSimulatorArm64()
    // ).forEach { iosTarget ->
    //     iosTarget.binaries.framework {
    //         baseName = "shared"
    //         isStatic = true
    //     }
    // }

    // TODO: Add JVM support later
    // jvm {
    //     testRuns.named("test") {
    //         executionTask.configure {
    //             useJUnitPlatform()
    //         }
    //     }
    // }

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

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)

                // Kotlinx DateTime
                implementation(libs.kotlinx.datetime)

                // Molecule
                implementation(libs.molecule.runtime)
//                implementation(libs.molecule.compose)

                // Navigation 3
                implementation(libs.navigation3.ui)
                implementation(libs.lifecycle.viewmodel.navigation3)
//                implementation(libs.adaptive.navigation3)

                // Database module (includes Room runtime transitively)
                implementation(project(":database"))
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        val androidMain by getting {
            dependencies {
                // Android-specific dependencies
            }
        }

        val jvmMain by getting {
            dependencies {
                // JVM-specific dependencies
                implementation(compose.desktop.currentOs)
            }
        }
    }

}

android {
    namespace = "com.greenrobotdev.linklibrary"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}
