package com.greenrobotdev.lordofthelinks.design.foundation.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.lordofthelinks.design.tokens.SpacingTokens

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