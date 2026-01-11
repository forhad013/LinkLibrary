package com.greenrobotdev.linklibrary.desktop

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.greenrobotdev.linklibrary.database.di.databaseModule
import com.greenrobotdev.linklibrary.screens.root.RootScreen
import org.koin.core.context.startKoin
import kotlin.system.exitProcess

fun main() {
    // Initialize Koin for desktop
    startKoin {
        modules(
            databaseModule,
        )
    }

    application {
        val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)
        Window(
            onCloseRequest = { exitProcess(0) },
            state = windowState,
            title = "Link Library"
        ) {
            MaterialTheme(colorScheme = darkColorScheme()) {
                RootScreen()
            }
        }
    }
}
