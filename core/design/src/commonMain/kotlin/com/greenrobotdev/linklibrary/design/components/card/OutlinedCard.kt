package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Outlined card component with Material 3 styling
 *
 * Card with outlined border appearance. Low-emphasis container for
 * content with minimal visual weight.
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to transparent
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param borderColor Border color. Defaults to theme's outline variant color
 * @param borderWidth Border width. Defaults to 1dp
 * @param onClick Optional click handler for interactive cards
 * @param content Content to be displayed inside the card
 */
@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            border = BorderStroke(borderWidth, borderColor),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor.copy(alpha = 0.38f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = androidx.compose.ui.unit.Dp(0.dp))
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            border = BorderStroke(borderWidth, borderColor),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor.copy(alpha = 0.38f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = androidx.compose.ui.unit.Dp(0.dp))
        ) {
            content()
        }
    }
}

/**
 * Simple outlined card - basic variant without click handling
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to transparent
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param borderColor Border color. Defaults to theme's outline variant color
 * @param borderWidth Border width. Defaults to 1dp
 * @param content Content to be displayed inside the card
 */
@Composable
fun SimpleOutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidth: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(borderWidth, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = contentColor.copy(alpha = 0.38f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = androidx.compose.ui.unit.Dp(0.dp))
    ) {
        content()
    }
}