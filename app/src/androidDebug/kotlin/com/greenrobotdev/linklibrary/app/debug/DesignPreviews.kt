package com.greenrobotdev.linklibrary.app.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.theme.AppTheme

/**
 * Material 3 Expressive Design Previews
 *
 * Comprehensive preview of all Material 3 components with the new Lords of Links theme
 * Purple theme (#4c22bd) with expressive animations and modern styling
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun Material3DesignPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Text(
                    text = "Lords of Links",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Material 3 Expressive Design System",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Buttons Section
                Text(
                    text = "Buttons",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {}) {
                        Text("Primary")
                    }
                    OutlinedButton(onClick = {}) {
                        Text("Outlined")
                    }
                    TextButton(onClick = {}) {
                        Text("Text")
                    }
                }

                // Icon Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Bookmark")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }

                // FAB
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(onClick = {}) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                    Text(
                        text = "Floating Action Button",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Text Fields
                Text(
                    text = "Input Fields",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search your library...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                )

                OutlinedTextField(
                    value = "Sample text",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Label") }
                )

                // Cards
                Text(
                    text = "Cards",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Card Title",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Card description text goes here with more details.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Checkboxes
                Text(
                    text = "Selection Controls",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = true, onCheckedChange = {})
                    Text("Selected")
                    Checkbox(checked = false, onCheckedChange = {})
                    Text("Not selected")
                }

                // Progress Indicators
                Text(
                    text = "Progress Indicators",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                        Text("Loading content...")
                    }
                }

                // Navigation Icons
                Text(
                    text = "Navigation Icons",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }
        }
    }
}

/**
 * Color Palette Preview
 * Shows all theme colors from the Lords of Links design
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 600)
@Composable
fun ColorPalettePreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Color Palette",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Primary colors
                ColorSwatch("Primary", MaterialTheme.colorScheme.primary)
                ColorSwatch("On Primary", MaterialTheme.colorScheme.onPrimary)
                ColorSwatch("Primary Container", MaterialTheme.colorScheme.primaryContainer)
                ColorSwatch("On Primary Container", MaterialTheme.colorScheme.onPrimaryContainer)

                // Secondary colors
                ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary)
                ColorSwatch("On Secondary", MaterialTheme.colorScheme.onSecondary)
                ColorSwatch("Secondary Container", MaterialTheme.colorScheme.secondaryContainer)
                ColorSwatch("On Secondary Container", MaterialTheme.colorScheme.onSecondaryContainer)

                // Surface colors
                ColorSwatch("Surface", MaterialTheme.colorScheme.surface)
                ColorSwatch("On Surface", MaterialTheme.colorScheme.onSurface)
                ColorSwatch("Surface Container", MaterialTheme.colorScheme.surfaceContainer)
                ColorSwatch("On Surface Variant", MaterialTheme.colorScheme.onSurfaceVariant)

                // Error colors
                ColorSwatch("Error", MaterialTheme.colorScheme.error)
                ColorSwatch("On Error", MaterialTheme.colorScheme.onError)
                ColorSwatch("Error Container", MaterialTheme.colorScheme.errorContainer)
                ColorSwatch("On Error Container", MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@Composable
private fun ColorSwatch(name: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = color,
                shape = MaterialTheme.shapes.small
            ) {}
        }
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Typography Preview
 * Shows all typography scales from Material 3
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TypographyPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Typography Scale",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text("Display Large", style = MaterialTheme.typography.displayLarge)
                Text("Display Medium", style = MaterialTheme.typography.displayMedium)
                Text("Display Small", style = MaterialTheme.typography.displaySmall)

                Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
                Text("Headline Medium", style = MaterialTheme.typography.headlineMedium)
                Text("Headline Small", style = MaterialTheme.typography.headlineSmall)

                Text("Title Large", style = MaterialTheme.typography.titleLarge)
                Text("Title Medium", style = MaterialTheme.typography.titleMedium)
                Text("Title Small", style = MaterialTheme.typography.titleSmall)

                Text("Body Large", style = MaterialTheme.typography.bodyLarge)
                Text("Body Medium", style = MaterialTheme.typography.bodyMedium)
                Text("Body Small", style = MaterialTheme.typography.bodySmall)

                Text("Label Large", style = MaterialTheme.typography.labelLarge)
                Text("Label Medium", style = MaterialTheme.typography.labelMedium)
                Text("Label Small", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
