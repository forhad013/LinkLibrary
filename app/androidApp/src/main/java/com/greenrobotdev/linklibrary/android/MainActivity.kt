package com.greenrobotdev.linklibrary.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.greenrobotdev.linklibrary.model.SharedContent as CommonSharedContent
import com.greenrobotdev.linklibrary.screens.root.RootScreen
import com.greenrobotdev.linklibrary.design.theme.LinkLibraryTheme

class MainActivity : ComponentActivity() {

    private var sharedContent by mutableStateOf<CommonSharedContent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if activity was started from a share intent
        handleIntent(intent)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CompositionLocalProvider {
                LinkLibraryTheme {
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

                sharedContent = CommonSharedContent(
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

                sharedContent = CommonSharedContent(
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
}

@Preview
@Composable
fun DefaultPreview() {
    LinkLibraryTheme {

    }
}
