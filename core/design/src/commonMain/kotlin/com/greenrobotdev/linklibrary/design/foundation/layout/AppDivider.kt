package com.greenrobotdev.linklibrary.design.foundation.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.tokens.SpacingTokens

/**
 * Foundation divider component with automatic theme integration
 *
 * This wrapper provides a simplified divider component that automatically integrates
 * with the Material 3 theme while offering sensible defaults for common use cases.
 *
 * @param modifier Modifier to be applied to the divider
 * @param color Color of the divider. Defaults to theme's outline variant color
 * @param thickness Thickness of the divider line. Defaults to 1dp
 * @param startIndent indentation before the divider line
 * @param endIndent indentation after the divider line
 */
@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp,
    startIndent: Dp = SpacingTokens.None,
    endIndent: Dp = SpacingTokens.None
) {
    val finalModifier = when {
        startIndent != SpacingTokens.None && endIndent != SpacingTokens.None ->
            modifier.padding(start = startIndent, end = endIndent)
        startIndent != SpacingTokens.None ->
            modifier.padding(start = startIndent)
        endIndent != SpacingTokens.None ->
            modifier.padding(end = endIndent)
        else -> modifier
    }

    Divider(
        modifier = finalModifier,
        color = color,
        thickness = thickness
    )
}

/**
 * Horizontal divider with standard spacing indentation
 */
@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}