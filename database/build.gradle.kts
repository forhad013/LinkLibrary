plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    jvm() // Add JVM target to enable expect/actual

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Room Database (KMP)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)

                // Koin for DI
                api(libs.koin.core)

                // Kotlinx DateTime
                implementation(libs.kotlinx.datetime)

                // Kotlinx Serialization
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val androidMain by getting {
            dependencies {
                // Room SQLite Wrapper for Android
                implementation(libs.androidx.room.sqlite.wrapper)
                implementation(libs.koin.android)

                // TODO: Add SQLCipher dependency when implementing encryption
                // SQLCipher for Android encryption requires custom SQLiteDriver implementation
                // implementation(libs.sqlcipher.android)
            }
        }

        val jvmMain by getting {
            dependencies {
                // Room SQLite for JVM
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.koin.core)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

// Configure Room schema directory per official docs
room {
    schemaDirectory("$projectDir/schemas")
}

// KSP dependencies for Room compiler
// Per official docs: https://developer.android.com/kotlin/multiplatform/room
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

// KSP configuration for Room incremental processing
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
    arg("room.useKspKotlinCodegen", "true")
}

android {
    namespace = "com.greenrobotdev.linklibrary.database"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
