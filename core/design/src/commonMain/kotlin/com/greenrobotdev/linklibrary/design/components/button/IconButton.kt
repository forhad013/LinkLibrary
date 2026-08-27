package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Icon button component with Material 3 styling
 *
 * Button that displays an icon for compact, icon-only interactions.
 * Uses theme's onSurfaceVariant color with circular tap target.
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 * @param tint Color tint for the icon. Defaults to theme's onSurfaceVariant
 */
@Composable
fun IconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    MaterialIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

/**
 * Icon button with filled tonal styling
 *
 * Icon button with filled background for higher visual emphasis.
 * Uses theme's secondaryContainer color with onSecondaryContainer content.
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 */
@Composable
fun FilledTonalIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

/**
 * Icon button with container surface styling
 *
 * Icon button with surface container background for medium emphasis.
 * Uses theme's surfaceContainerHighest color with onSurface content.
 *
 * @param icon ImageVector to display as the icon
 * @param contentDescription Content description for accessibility
 * @param onClick Callback invoked when button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled for interaction
 */
@Composable
fun SurfaceIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}