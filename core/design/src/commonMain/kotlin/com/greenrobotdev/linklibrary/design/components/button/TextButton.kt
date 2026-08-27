package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton as MaterialTextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Text button component with Material 3 styling
 *
 * Low-emphasis button for tertiary actions. Uses text-only style with
 * theme's primary color for content, minimal visual weight.
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = ButtonDefaults.IconSpacing)
            )
        }
        Text(text)
    }
}

/**
 * Text button with destructive/error styling
 *
 * Low-emphasis button for destructive actions. Uses error color scheme
 * to indicate potential destructive consequences with minimal visual weight.
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun DestructiveTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    MaterialTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.error,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = ButtonDefaults.IconSpacing)
            )
        }
        Text(text)
    }
}