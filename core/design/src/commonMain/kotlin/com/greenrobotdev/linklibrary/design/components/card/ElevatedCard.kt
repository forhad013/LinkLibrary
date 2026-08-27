package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * Elevated card component with Material 3 styling
 *
 * Card with elevated appearance using tonal elevation. Medium-emphasis
 * container for related content with subtle depth.
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to theme's surface container low color
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param elevation Card elevation level. Defaults to Level1 (1dp)
 * @param onClick Optional click handler for interactive cards
 * @param content Content to be displayed inside the card
 */
@Composable
fun ElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    elevation: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp(1.dp),
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
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
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
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            content()
        }
    }
}

/**
 * Elevated card with border outline
 *
 * @param modifier Modifier to be applied to the card
 * @param shape Shape to be applied to the card. Defaults to theme's medium shape
 * @param backgroundColor Background color. Defaults to theme's surface container low color
 * @param contentColor Content color. Defaults to theme's onSurface color
 * @param borderBorderColor Border color. Defaults to theme's outline variant color
 * @param elevation Card elevation level. Defaults to Level1 (1dp)
 * @param onClick Optional click handler for interactive cards
 * @param content Content to be displayed inside the card
 */
@Composable
fun ElevatedBorderedCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderBorderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    elevation: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp(1.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = borderBorderColor
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = borderBorderColor
            ),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            content()
        }
    }
}