package com.greenrobotdev.lordofthelinks.desktop

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.greenrobotdev.lordofthelinks.database.di.databaseModule
import com.greenrobotdev.lordofthelinks.di.appModule
import com.greenrobotdev.lordofthelinks.screens.root.RootScreen
import org.koin.core.context.startKoin
import kotlin.system.exitProcess

fun main() {
    // Initialize Koin for desktop
    startKoin {
        modules(
            appModule,       // HTTP client, metadata fetch service
            databaseModule,  // Data repositories
        )
    }

    application {
        val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)
        Window(
            onCloseRequest = { exitProcess(0) },
            state = windowState,
            title = "Link Library"
        ) {
            MaterialTheme() {
                RootScreen()
            }
        }
    }
}
