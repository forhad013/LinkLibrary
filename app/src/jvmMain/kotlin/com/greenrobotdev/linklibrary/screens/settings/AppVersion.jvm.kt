package com.greenrobotdev.linklibrary.screens.settings

import androidx.compose.runtime.Composable

// shortcut: desktop has no packaged version yet; read it from the build when desktop ships
@Composable
actual fun rememberAppVersion(): String = "dev"
