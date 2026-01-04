plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":app"))
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