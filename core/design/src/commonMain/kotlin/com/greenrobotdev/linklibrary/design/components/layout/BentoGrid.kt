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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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

    LazyVerticalGrid(
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

/**
 * Horizontal bento grid for horizontal scrolling layouts
 */
@Composable
fun <T> BentoRow(
    items: List<BentoItem<T>>,
    modifier: Modifier = Modifier,
    itemWidth: Dp = 200.dp,
    horizontalSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemContent: @Composable (item: BentoItem<T>, index: Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
    ) {
        items(
            items = items,
            key = { it.key ?: "bento_row_item_${items.indexOf(it)}" }
        ) { item ->
            val index = items.indexOf(item)

            val enterTransition = fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 50
                )
            ) + slideInHorizontally(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) { it }

            AnimatedVisibility(
                visible = true,
                enter = enterTransition
            ) {
                Box(
                    modifier = Modifier
                        .width(itemWidth)
                        .let {
                            when (item.size) {
                                BentoItemSize.Small -> it.height(100.dp)
                                BentoItemSize.Medium -> it.height(140.dp)
                                BentoItemSize.Large -> it.height(200.dp)
                                BentoItemSize.Full -> it.height(180.dp)
                            }
                        }
                ) {
                    itemContent(item, index)
                }
            }
        }
    }
}

/**
 * Masonry-style bento grid for varied heights
 */
@Composable
fun <T> BentoMasonry(
    items: List<BentoItem<T>>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemContent: @Composable (item: BentoItem<T>, index: Int) -> Unit
) {
    // Split items into columns for masonry layout
    val columnItems = remember(items, columns) {
        List(columns) { colIndex ->
            items.filterIndexed { index, _ ->
                index % columns == colIndex
            }
        }
    }

    Row(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
    ) {
        repeat(columns) { colIndex ->
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(verticalSpacing)
            ) {
                columnItems[colIndex].forEach { item ->
                    val index = items.indexOf(item)

                    val enterTransition = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing,
                            delayMillis = index * 30
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
                                        BentoItemSize.Medium -> it.height(180.dp)
                                        BentoItemSize.Large -> it.height(240.dp)
                                        BentoItemSize.Full -> it.height(200.dp)
                                    }
                                }
                        ) {
                            itemContent(item, index)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Responsive bento grid that adapts columns based on available width
 */
@Composable
fun <T> BentoGridResponsive(
    items: List<BentoItem<T>>,
    modifier: Modifier = Modifier,
    minColumnWidth: Dp = 180.dp,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemContent: @Composable (item: BentoItem<T>, index: Int) -> Unit
) {
    // Calculate number of columns based on available width
    var containerWidth by remember { mutableStateOf(0) }

    val columns = remember(containerWidth, minColumnWidth) {
        maxOf(
            1,
            (containerWidth / minColumnWidth.value).toInt()
        )
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                containerWidth = coordinates.size.width
            }
    ) {
        BentoGrid(
            items = items,
            columns = columns,
            horizontalSpacing = horizontalSpacing,
            verticalSpacing = verticalSpacing,
            contentPadding = contentPadding,
            itemContent = itemContent
        )
    }
}

/**
 * Staggered bento grid with custom animation delays
 */
@Composable
fun <T> BentoGridStaggered(
    items: List<BentoItem<T>>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    staggerDelay: Int = 100,
    itemContent: @Composable (item: BentoItem<T>, index: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        items(
            items = items,
            key = { it.key ?: "bento_staggered_${items.indexOf(it)}" }
        ) { item ->
            val index = items.indexOf(item)

            val enterTransition = fadeIn(
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * staggerDelay
                )
            ) + scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                initialScale = 0.8f
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
                                BentoItemSize.Large -> it.height(280.dp)
                                BentoItemSize.Full -> it.height(200.dp)
                            }
                        }
                ) {
                    itemContent(item, index)
                }
            }
        }
    }
}

/**
 * Empty state for bento grids
 */
@Composable
fun BentoEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            actionLabel?.let {
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(it)
                }
            }
        }
    }
}