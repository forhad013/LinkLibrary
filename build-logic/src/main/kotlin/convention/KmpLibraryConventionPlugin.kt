package convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPlugin

/**
 * Convention plugin for Kotlin Multiplatform library modules.
 * Applies standard KMP configuration with Android and JVM targets,
 * Compose Multiplatform support, and common compiler options.
 *
 * AGP 9 compatible: Uses com.android.kotlin.multiplatform.library plugin
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            pluginManager.apply(KotlinMultiplatformPlugin::class.java)
            // AGP 9: Apply the Android KMP library plugin
            pluginManager.apply("com.android.kotlin.multiplatform.library")

            // Configure KMP extension
            extensions.configure<KotlinMultiplatformExtension> {
                // Standard targets for KMP library
                androidTarget {
                    configureAndroidTarget()
                }

                jvm("desktop") {
                    configureJvmTarget()
                }

                // Source sets configuration
                sourceSets {
                    commonMain.dependencies {
                        implementation(libs.findLibrary("compose.runtime").get())
                        implementation(libs.findLibrary("compose.foundation").get())
                        implementation(libs.findLibrary("compose.material3").get())
                        implementation(libs.findLibrary("compose.ui").get())
                        implementation(libs.findLibrary("koin.core").get())
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("compose.ui.tooling.preview").get())
                    }

                    val desktopMain by getting {
                        dependencies {
                            implementation(compose.desktop.currentOs)
                            implementation(libs.findLibrary("koin.jvm").get())
                        }
                    }
                }
            }

            // Configure Android extension
            extensions.configure<LibraryExtension> {
                configureAndroidLibrary()
            }
        }
    }
}

private fun KotlinJvmOptions.configureJvmTarget() {
    jvmTarget = "17"
    freeCompilerArgs.addAll(
        "-Xjsr305=strict",
        "-Xinline-optimizations"
    )
}

private fun org.jetbrains.kotlin.gradle.targets.android.KotlinAndroidTarget.configureAndroidTarget() {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        freeCompilerArgs.add("-Xcontext-receivers")
        freeCompilerArgs.add("-Xinline-optimizations")
    }
}

private fun LibraryExtension.configureAndroidLibrary() {
    compileSdk = 36
    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}