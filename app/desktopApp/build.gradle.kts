plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                // Project dependencies
                implementation(project(":app"))
                implementation(project(":database"))

                // Koin
                implementation(libs.koin.core)

                // Kotlinx
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.swing) // Main dispatcher for JVM

                // Molecule
                implementation(libs.molecule.runtime)
            }
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.greenrobotdev.linklibrary.desktop.MainKt"

        nativeDistributions {
            targetFormats = setOf(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Rpm
            )

            packageVersion = "1.0.0"
            description = "Link Library - Manage your bookmarks"
            copyright = "© 2025 Green Robot Dev. All rights reserved."
            vendor = "Green Robot Dev"

            macOS {
                bundleID = "com.greenrobotdev.linklibrary.desktop"
                // iconFile = project.file("src/jvmMain/resources/icon.icns")
            }

            windows {
                menuGroup = "Link Library"
                shortcut = true
                // iconFile = project.file("src/jvmMain/resources/icon.ico")
            }

            linux {
                packageName = "link-library"
                shortcut = true
                // iconFile = project.file("src/jvmMain/resources/icon.png")
            }
        }
    }
}

// Task to run the desktop app
tasks.register<JavaExec>("runDesktop") {
    group = "application"
    description = "Run the desktop application"

    dependsOn(":app:desktopApp:jvmJar")

    val jvmJar = tasks.getByName("jvmJar")
    classpath(jvmJar.outputs.files)

    // Set main class
    systemProperty("mainClass", "com.greenrobotdev.linklibrary.desktop.MainKt")

    // JVM args for Compose Desktop
    jvmArgs(
        "--add-opens", "java.desktop/sun.awt=Xinternal.reflection",
        "--add-opens", "java.desktop/java.awt.peer=Xinternal.reflection",
        "-Dapple.awt.application.appearance=system"
    )
}
