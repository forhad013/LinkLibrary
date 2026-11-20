plugins {
    alias(libs.plugins.androidApplication)
    // Note: kotlinAndroid plugin removed - AGP 9 provides built-in Kotlin support
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

android {
    namespace = "com.greenrobotdev.lordofthelinks.android"
    compileSdk = 37
    defaultConfig {
        applicationId = "com.greenrobotdev.lordofthelinks.android"
        minSdk = 28
        targetSdk = 35
        versionCode = providers.gradleProperty("app.versionCode").get().toInt()
        versionName = providers.gradleProperty("app.versionName").get()
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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
    implementation(project(":core:utils"))
    implementation(project(":core:model"))
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