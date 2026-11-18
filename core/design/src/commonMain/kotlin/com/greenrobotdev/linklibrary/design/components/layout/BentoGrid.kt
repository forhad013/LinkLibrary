package com.greenrobotdev.linklibrary.design.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * BentoGrid - Responsive grid layout system for Material 3 Expressive
 *
 * Bento-style grid layout that adapts to different screen sizes and content types:
 * - Responsive column calculations
 * - Animated item transitions
 * - Support for different item sizes
 * - Staggered animations
 * - Accessible layout system
 */

sealed class BentoItemSize {
    data object Small : BentoItemSize()
    data object Medium : BentoItemSize()
    data object Large : BentoItemSize()
    data object Full : BentoItemSize()
}

data class BentoItem<T>(
    val data: T,
    val size: BentoItemSize = BentoItemSize.Medium,
    val key: String? = null
)

@Composable
fun <T> BentoGrid(
    items: List<BentoItem<T>>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemContent: @Composable (item: BentoItem<T>, index: Int) -> Unit
) {
    // Calculate span based on item size
    fun getSpan(size: BentoItemSize): Int {
        return when (size) {
            BentoItemSize.Small -> 1
            BentoItemSize.Medium -> 1
            BentoItemSize.Large -> 2
            BentoItemSize.Full -> columns
        }
    }

    // Calculate row span for large items
    fun getRowSpan(size: BentoItemSize): Int {
        return when (size) {
            BentoItemSize.Small -> 1
            BentoItemSize.Medium -> 1
            BentoItemSize.Large -> 2
            BentoItemSize.Full -> 1
        }
    }

    AppLazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        items(
            items = items,
            key = { it.key ?: "bento_item_${items.indexOf(it)}" }
        ) { item ->
            val index = items.indexOf(item)
            val span = getSpan(item.size)

            // Animate item appearance
            val enterTransition = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 50 // Stagger effect
                )
            ) + expandVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            AnimatedVisibility(
                visible = true,
                enter = enterTransition
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let {
                            when (item.size) {
                                BentoItemSize.Small -> it.height(120.dp)
                                BentoItemSize.Medium -> it.height(160.dp)
                                BentoItemSize.Large -> it
                                    .fillMaxWidth()
                                    .height(320.dp)
                                BentoItemSize.Full -> it
                                    .fillMaxWidth()
                                    .height(200.dp)
                            }
                        }
                ) {
                    itemContent(item, index)
                }
            }
        }
    }
}

