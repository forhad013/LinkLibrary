package com.greenrobotdev.lordofthelinks.screens.settings

import androidx.compose.runtime.Composable

/** The installed app's version name ("1.0"), for Settings → About. */
@Composable
expect fun rememberAppVersion(): String
