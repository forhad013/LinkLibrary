package convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for testing configuration.
 * Configures common testing dependencies and settings
 * for Kotlin Multiplatform projects.
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    val commonTest by getting {
                        dependencies {
                            implementation(libs.findLibrary("kotlin.test").get())
                            implementation(libs.findLibrary("kotest.assertions").get())
                        }
                    }

                    val androidUnitTest by getting {
                        dependencies {
                            implementation(libs.findLibrary("junit").get())
                            implementation(libs.findLibrary("mockk.android").get())
                        }
                    }

                    val androidInstrumentedTest by getting {
                        dependencies {
                            implementation(libs.findLibrary("androidx.test.ext.junit").get())
                            implementation(libs.findLibrary("androidx.test.espresso").get())
                            implementation(libs.findLibrary("compose.ui.test.junit4").get())
                        }
                    }

                    val desktopTest by getting {
                        dependencies {
                            implementation(libs.findLibrary("kotest.runner.junit5").get())
                            implementation(libs.findLibrary("mockk").get())
                        }
                    }
                }
            }
        }
    }
}