package com.greenrobotdev.linklibrary.design.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.theme.DarkColorScheme
import com.greenrobotdev.linklibrary.design.theme.LightColorScheme

// ─────────────────────────────────────────────────────────────
// Preview Functions (Android Debug Only)
// ─────────────────────────────────────────────────────────────

/**
 * Preview wrapper for TheLordsOfLinks design system components
 */
@Composable
private fun DesignPreviewWrapper(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

// FormCard Previews
@Preview(name = "FormCard - Light Theme")
@Composable
fun FormCardPreviewLight() {
    DesignPreviewWrapper(darkTheme = false) {
        FormCard(
            headerIcon = Icons.Default.Link,
            headerTitle = "Link Details"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "https://example.com/article",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "My Awesome Article",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(name = "FormCard - Dark Theme", showBackground = true, widthDp = 400)
@Composable
fun FormCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        FormCard(
            headerIcon = Icons.Default.Link,
            headerTitle = "Link Details"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "https://example.com/article",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "My Awesome Article",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



/*

// ToggleCard Previews
@Preview(name = "ToggleCard - Enabled", showBackground = true, widthDp = 400)
@Composable
public fun ToggleCardPreviewEnabled() {
    DesignPreviewWrapper(darkTheme = false) {
        var isEnabled by remember { mutableStateOf(true) }

        ToggleCard(
            isEnabled = isEnabled,
            onEnabledChange = { isEnabled = it },
            title = "Add to Tasks",
            description = "Set priority and due date",
            enabledIcon = Icons.Default.AutoAwesome,
            disabledIcon = Icons.Default.Schedule,
            activeColor = MaterialTheme.colorScheme.primary
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Priority: High",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Due: Tomorrow",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(name = "ToggleCard - Disabled", showBackground = true, widthDp = 400)
@Composable
public fun ToggleCardPreviewDisabled() {
    DesignPreviewWrapper(darkTheme = false) {
        var isEnabled by remember { mutableStateOf(false) }

        ToggleCard(
            isEnabled = isEnabled,
            onEnabledChange = { isEnabled = it },
            title = "Add to Tasks",
            description = "Set priority and due date",
            enabledIcon = Icons.Default.AutoAwesome,
            disabledIcon = Icons.Default.Schedule
        )
    }
}

@Preview(name = "ToggleCard - Dark Theme", showBackground = true, widthDp = 400)
@Composable
public fun ToggleCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        var isEnabled by remember { mutableStateOf(true) }

        ToggleCard(
            isEnabled = isEnabled,
            onEnabledChange = { isEnabled = it },
            title = "Add to Tasks",
            description = "Set priority and due date",
            enabledIcon = Icons.Default.AutoAwesome,
            disabledIcon = Icons.Default.Schedule
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Priority: High",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Due: Tomorrow",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// CompactFormCard Previews
@Preview(name = "CompactFormCard", showBackground = true, widthDp = 350)
@Composable
public fun CompactFormCardPreview() {
    DesignPreviewWrapper(darkTheme = false) {
        CompactFormCard(
            headerIcon = Icons.Default.Folder,
            headerTitle = "Collections"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "• Work Resources",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Personal Projects",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(name = "CompactFormCard - Dark", showBackground = true, widthDp = 350)
@Composable
public fun CompactFormCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        CompactFormCard(
            headerIcon = Icons.Default.Folder,
            headerTitle = "Collections"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "• Work Resources",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Personal Projects",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// InputCard Previews
@Preview(name = "InputCard", showBackground = true, widthDp = 400)
@Composable
public fun InputCardPreview() {
    DesignPreviewWrapper(darkTheme = false) {
        InputCard(
            title = "Link Information"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "URL",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "https://example.com",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(name = "InputCard - Dark", showBackground = true, widthDp = 400)
@Composable
public fun InputCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        InputCard(
            title = "Link Information"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "URL",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "https://example.com",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ContentCard Previews
@Preview(name = "ContentCard", showBackground = true, widthDp = 400)
@Composable
public fun ContentCardPreview() {
    DesignPreviewWrapper(darkTheme = false) {
        ContentCard(
            headerIcon = Icons.Default.Folder,
            headerTitle = "Organization"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = Icons.Default.Folder,
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        variant = IconBackgroundVariant.Compact
                    )
                    Text(
                        text = "Work Resources",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = Icons.Default.Folder,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        variant = IconBackgroundVariant.Compact
                    )
                    Text(
                        text = "Personal Projects",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(name = "ContentCard - Dark", showBackground = true, widthDp = 400)
@Composable
public fun ContentCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        ContentCard(
            headerIcon = Icons.Default.Folder,
            headerTitle = "Organization"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = Icons.Default.Folder,
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        variant = IconBackgroundVariant.Compact
                    )
                    Text(
                        text = "Work Resources",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        icon = Icons.Default.Folder,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        variant = IconBackgroundVariant.Compact
                    )
                    Text(
                        text = "Personal Projects",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// SectionContentCard Previews
@Preview(name = "SectionContentCard", showBackground = true, widthDp = 400)
@Composable
public fun SectionContentCardPreview() {
    DesignPreviewWrapper(darkTheme = false) {
        SectionContentCard(
            title = "Personal Notes"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "This is an important article about Kotlin Multiplatform development. Contains useful information about KMP architecture and best practices.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(name = "SectionContentCard - Dark", showBackground = true, widthDp = 400)
@Composable
public fun SectionContentCardPreviewDark() {
    DesignPreviewWrapper(darkTheme = true) {
        SectionContentCard(
            title = "Personal Notes"
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "This is an important article about Kotlin Multiplatform development. Contains useful information about KMP architecture and best practices.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Comprehensive Preview - All Cards
@Preview(name = "All Cards Showcase", showBackground = true, widthDp = 400, heightDp = 1200)
@Composable
public fun AllCardsShowcasePreview() {
    DesignPreviewWrapper(darkTheme = false) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormCard(
                headerIcon = Icons.Default.Link,
                headerTitle = "Link Details"
            ) {
                Text("Example link content", style = MaterialTheme.typography.bodyMedium)
            }

            var taskEnabled by remember { mutableStateOf(true) }
            ToggleCard(
                isEnabled = taskEnabled,
                onEnabledChange = { taskEnabled = it },
                title = "Add to Tasks",
                enabledIcon = Icons.Default.AutoAwesome,
                disabledIcon = Icons.Default.Schedule
            )

            CompactFormCard(
                headerIcon = Icons.Default.Folder,
                headerTitle = "Collections"
            ) {
                Text("Example collections", style = MaterialTheme.typography.bodySmall)
            }

            InputCard(title = "Link Information") {
                Text("Example input content", style = MaterialTheme.typography.bodyMedium)
            }

            ContentCard(
                headerIcon = Icons.Default.Add,
                headerTitle = "Tags"
            ) {
                Text("Example tags content", style = MaterialTheme.typography.bodyMedium)
            }

            SectionContentCard(title = "Notes") {
                Text("Example notes content", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }


}
 */
