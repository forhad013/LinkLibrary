plugins {
    alias(libs.plugins.androidApplication)
    // Note: kotlinAndroid plugin removed - AGP 9 provides built-in Kotlin support
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    namespace = "com.greenrobotdev.linklibrary.android"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.greenrobotdev.linklibrary.android"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }

    // Enable resource processing for icons
    sourceSets {
        getByName("main").res.srcDirs("src/main/res")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // Note: kotlinOptions removed - Kotlin compiler options now configured differently in AGP 9
    // Use tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> for compiler options if needed
}

dependencies {
    implementation(project(":app"))
    implementation(project(":bookmarks"))
    implementation(project(":core:design"))
    implementation(project(":database"))
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.androidx.activity.compose)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.uiTooling)
    implementation(compose.materialIconsExtended)
    debugImplementation(libs.compose.ui.tooling)
}