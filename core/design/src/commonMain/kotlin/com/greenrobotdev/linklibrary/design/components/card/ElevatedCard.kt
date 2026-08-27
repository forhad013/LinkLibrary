package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Elevated card component with Material 3 Expressive features
 *
 * Card with elevated appearance and enhanced animations:
 * - Hover elevation animation (0→8dp on desktop)
 * - Press scale effect (0.98x when pressed)
 * - Smooth elevation transitions
 * - Ripple effects on click
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
    elevation: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    // Expressive: Interaction source for animated cards
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press scale effect
    val scale by animateFloatAsState(
        targetValue = if (onClick != null && isPressed) 0.98f else 1f,
        animationSpec = SpringPhysics.gentleSpring(),
        label = "elevated_card_scale"
    )

    // Expressive: Dynamic elevation animation
    val animatedElevation by animateDpAsState(
        targetValue = when {
            onClick != null && isPressed -> elevation + 4.dp  // Elevate further when pressed
            onClick != null -> elevation + 2.dp  // Slight elevation when interactive
            else -> elevation
        },
        animationSpec = SpringPhysics.gentleSpring(),
        label = "elevated_card_elevation"
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.scale(scale),
            shape = shape,
            interactionSource = interactionSource,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = animatedElevation,
                pressedElevation = elevation + 4.dp,
                hoveredElevation = elevation + 2.dp,
                focusedElevation = elevation + 2.dp
            )
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
            elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
        ) {
            content()
        }
    }
}

/**
 * Elevated card with border outline and expressive features
 *
 * Card with border and enhanced animations:
 * - Press scale effect with spring physics
 * - Dynamic elevation with border transitions
 * - Border color animations
 * - Smooth visual feedback
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
    elevation: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press scale effect
    val scale by animateFloatAsState(
        targetValue = if (onClick != null && isPressed) 0.98f else 1f,
        animationSpec = SpringPhysics.gentleSpring(),
        label = "bordered_card_scale"
    )

    // Expressive: Dynamic elevation
    val animatedElevation by animateDpAsState(
        targetValue = when {
            onClick != null && isPressed -> elevation + 4.dp
            onClick != null -> elevation + 2.dp
            else -> elevation
        },
        animationSpec = SpringPhysics.gentleSpring(),
        label = "bordered_card_elevation"
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.scale(scale),
            shape = shape,
            border = BorderStroke(width = 1.dp, color = borderBorderColor),
            interactionSource = interactionSource,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = animatedElevation,
                pressedElevation = elevation + 4.dp,
                hoveredElevation = elevation + 2.dp,
                focusedElevation = elevation + 2.dp
            )
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            border = BorderStroke(width = 1.dp, color = borderBorderColor),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = backgroundColor,
                disabledContentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
        ) {
            content()
        }
    }
}