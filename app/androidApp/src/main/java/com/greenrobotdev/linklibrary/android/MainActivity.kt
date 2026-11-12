package com.greenrobotdev.linklibrary.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import android.os.Build
import com.greenrobotdev.linklibrary.bookmarks.model.SharedContent
import com.greenrobotdev.linklibrary.design.theme.LinkLibraryTheme
import com.greenrobotdev.linklibrary.screens.root.RootScreen
import com.greenrobotdev.linklibrary.utils.settings.SettingsFactory
import com.greenrobotdev.linklibrary.utils.preferences.ThemePreferenceManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var sharedContent by mutableStateOf<SharedContent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SettingsFactory with application context
        SettingsFactory.initialize(this)

        // Check if activity was started from a share intent
        handleIntent(intent)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val themePreferenceManager = remember { ThemePreferenceManager() }
            val coroutineScope = rememberCoroutineScope()

            // Reactive theme state that updates when theme preference changes
            var darkTheme by remember { mutableStateOf(false) }

            // Load theme preference on startup
            LaunchedEffect(Unit) {
                val isDarkMode = themePreferenceManager.isDarkModeEnabled()
                darkTheme = isDarkMode ?: isSystemInDarkTheme()

                // Continuously check for theme changes every second
                // This ensures theme updates are applied immediately without app restart
                while (true) {
                    kotlinx.coroutines.delay(1000) // Check every second
                    val newIsDarkMode = themePreferenceManager.isDarkModeEnabled()
                    val newDarkTheme = newIsDarkMode ?: isSystemInDarkTheme()
                    if (darkTheme != newDarkTheme) {
                        darkTheme = newDarkTheme
                    }
                }
            }

            CompositionLocalProvider {
                LinkLibraryTheme(darkTheme = darkTheme) {
                    RootScreen(
                        sharedContent = sharedContent,
                        onSharedContentHandled = { sharedContent = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    /**
     * Parse incoming intent and extract shared content
     */
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                // Handle single item being shared
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)

                // Extract URL from text if present
                val url = extractUrlFromText(text) ?: uri?.toString()

                sharedContent = SharedContent(
                    url = url,
                    text = text,
                    title = title
                )
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                // Handle multiple items being shared
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)

                // For multiple items, just take the first text we can find
                val url = extractUrlFromText(text)

                sharedContent = SharedContent(
                    url = url,
                    text = text,
                    title = title
                )
            }
        }
    }

    /**
     * Extract URL from text using regex pattern
     */
    private fun extractUrlFromText(text: String?): String? {
        if (text.isNullOrEmpty()) return null

        // Common URL patterns
        val urlPatterns = listOf(
            Regex("(https?://[^\\s]+)"),
            Regex("(www\\.[^\\s]+)"),
            Regex("([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}[^\\s]*)")
        )

        for (pattern in urlPatterns) {
            val match = pattern.find(text)
            if (match != null) {
                var url = match.value
                // Add https:// if missing
                if (!url.startsWith("http") && !url.startsWith("www")) {
                    url = "https://$url"
                } else if (url.startsWith("www")) {
                    url = "https://$url"
                }
                return url
            }
        }

        return null
    }

    /**
     * Check if system is in dark mode
     */
    private fun isSystemInDarkTheme(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Use night mode flag for API 29+
                val nightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
            else -> {
                // Fallback for older devices - assume light mode
                // This is a reasonable default as most modern devices support API 29+
                false
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    LinkLibraryTheme {

    }
}
