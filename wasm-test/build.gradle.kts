plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    js("wasm") {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            binaries.executable()
        }
    }

    sourceSets {
        val wasmMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
            }
        }
    }
}

compose {
    // Configure compose for WASM
}