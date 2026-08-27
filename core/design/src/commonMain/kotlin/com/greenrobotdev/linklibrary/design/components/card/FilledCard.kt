package com.greenrobotdev.linklibrary.design.components.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.expressive.animation.SpringPhysics

/**
 * Filled card component with Material 3 Expressive features
 *
 * Card with filled background and enhanced animations:
 * - Press scale effect (0.98x when pressed)
 * - Subtle background color transitions
 * - Smooth visual feedback
 * - Enhanced ripple effects
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press scale effect
    val scale by animateFloatAsState(
        targetValue = if (onClick != null && isPressed) 0.98f else 1f,
        animationSpec = SpringPhysics.gentleSpring(),
        label = "filled_card_scale"
    )

    // Expressive: Background color transition
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = SpringPhysics.gentleSpring(),
        label = "filled_card_background"
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.scale(scale),
            shape = shape,
            interactionSource = interactionSource,
            colors = CardDefaults.cardColors(
                containerColor = animatedBackgroundColor,
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
                containerColor = animatedBackgroundColor,
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
 * Filled card with primary color background and expressive features
 *
 * High-emphasis card with primary color and enhanced animations:
 * - Press scale effect with bouncy spring
 * - Primary color transitions
 * - Strong visual feedback
 * - Enhanced interactive states
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Expressive: Press scale effect with bouncy spring for high-emphasis
    val scale by animateFloatAsState(
        targetValue = if (onClick != null && isPressed) 0.97f else 1f,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "primary_filled_card_scale"
    )

    // Expressive: Primary color transitions
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = SpringPhysics.bouncySpring(),
        label = "primary_filled_card_background"
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.scale(scale),
            shape = shape,
            interactionSource = interactionSource,
            colors = CardDefaults.cardColors(
                containerColor = animatedBackgroundColor,
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
                containerColor = animatedBackgroundColor,
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