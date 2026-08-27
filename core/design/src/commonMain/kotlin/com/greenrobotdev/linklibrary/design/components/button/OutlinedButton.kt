package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton as MaterialOutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Outlined button component with Material 3 styling
 *
 * Medium-emphasis button for secondary actions. Uses outlined style with
 * theme's primary color for border and content.
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    MaterialOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
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
 * Outlined button with destructive/error styling
 *
 * High-emphasis button for destructive actions like delete or remove.
 * Uses error color scheme to indicate potential destructive consequences.
 *
 * @param text Button text label
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param icon Optional leading icon to display before text
 */
@Composable
fun DestructiveOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    MaterialOutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
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