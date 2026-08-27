plugins {
    alias(libs.plugins.convention.android.room)
}

kotlin {
    android {
        namespace = "com.greenrobotdev.linklibrary.database"
        compileSdk = 37
    }
    jvm()

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

// Configure Room schema directory
configure<androidx.room.gradle.RoomExtension> {
    schemaDirectory("$projectDir/schemas")
}

// Configure KSP compiler args for Room
configure<com.google.devtools.ksp.gradle.KspExtension> {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
    arg("room.useKspKotlinCodegen", "true")
}

// Configure KSP dependencies for Room compiler
dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

