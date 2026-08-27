package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Filled card component with Material 3 styling
 *
 * Card with filled background appearance. High-emphasis container for
 * important content with strong visual presence.
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to theme's surface container highest color
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param onClick Optional click handler for interactive cards
 * @param content Content to be displayed inside the card
 */
@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    }
}

/**
 * Filled card with primary color background
 *
 * High-emphasis card with theme primary color for special prominence.
 * Use sparingly for highly important content.
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to theme's primary container color
 * @param contentColor Content color. Defaults to theme's onPrimaryContainer color
 * @param onClick Optional click handler for interactive cards
 * @param content Content to be displayed inside the card
 */
@Composable
fun PrimaryFilledCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor.copy(alpha = 0.38f),
                disabledContentColor = contentColor.copy(alpha = 0.38f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor.copy(alpha = 0.38f),
                disabledContentColor = contentColor.copy(alpha = 0.38f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            content()
        }
    }
}