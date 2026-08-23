package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for WASM (WebAssembly) target configuration.
 * Configures WASM-specific settings for Kotlin Multiplatform projects.
 */
class WasmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                js("wasm") {
                    browser {
                        commonWebpackConfig {
                            cssSupport {
                                enabled.set(true)
                            }
                            outputModuleName.set("linkLibrary")
                        }
                        binaries.executable()
                    }
                }

                sourceSets {
                    val wasmMain by getting {
                        dependencies {
                            implementation(compose.html.core)
                            implementation(compose.runtime)
                            implementation(compose.foundation)
                            implementation(compose.material3)
                            implementation(compose.ui)
                            implementation(compose.materialIconsExtended)
                        }
                    }
                }
            }
        }
    }
}