package com.greenrobotdev.linklibrary.design.motion

import androidx.compose.runtime.Composable

// shortcut: no system setting read here yet; read the platform's reduce-motion setting when this target ships
@Composable
actual fun rememberReducedMotion(): Boolean = false
