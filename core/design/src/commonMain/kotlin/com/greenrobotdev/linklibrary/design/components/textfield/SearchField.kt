package com.greenrobotdev.linklibrary.design.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField as MaterialOutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Search field component with Material 3 styling
 *
 * Specialized text input field optimized for search functionality with
 * search icon and proper theming. Supports both String and TextFieldValue.
 *
 * @param query Current search query
 * @param onQueryChange Callback invoked when query changes
 * @param modifier Modifier to be applied to the search field
 * @param placeholder Optional placeholder text (defaults to "Search...")
 * @param enabled Whether the field is enabled for input
 * @param singleLine Whether to restrict to single line (default true)
 */
@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
    )

    MaterialOutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        placeholder = {
            androidx.compose.material3.Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = singleLine,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}

/**
 * Search field with TextFieldValue support for advanced cursor management
 *
 * @param query Current search query as TextFieldValue
 * @param onQueryChange Callback invoked when query changes
 * @param modifier Modifier to be applied to the search field
 * @param placeholder Optional placeholder text (defaults to "Search...")
 * @param enabled Whether the field is enabled for input
 * @param singleLine Whether to restrict to single line (default true)
 */
@Composable
fun SearchField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
    )

    MaterialOutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        readOnly = false,
        placeholder = {
            androidx.compose.material3.Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = singleLine,
        colors = colors,
        shape = MaterialTheme.shapes.small
    )
}