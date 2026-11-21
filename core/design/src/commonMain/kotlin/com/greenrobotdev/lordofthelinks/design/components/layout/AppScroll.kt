package com.greenrobotdev.lordofthelinks.design.components.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
 * The app's scroll containers. Use these instead of LazyColumn / LazyRow / LazyVerticalGrid /
 * verticalScroll / horizontalScroll.
 *
 * They turn off the Android stretch overscroll: while the stretch settles, the next tap only stops
 * it, so buttons near the end of a scrolled list (the Filters sheet's switches, the Feed's cards)
 * seemed dead until the list was nudged.
 */

/** [LazyColumn] without the stretch overscroll. */
@Composable
fun AppLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) = LazyColumn(
    modifier = modifier,
    state = state,
    contentPadding = contentPadding,
    reverseLayout = reverseLayout,
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    flingBehavior = flingBehavior,
    userScrollEnabled = userScrollEnabled,
    overscrollEffect = null,
    content = content,
)

/** [LazyRow] without the stretch overscroll. */
@Composable
fun AppLazyRow(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) = LazyRow(
    modifier = modifier,
    state = state,
    contentPadding = contentPadding,
    reverseLayout = reverseLayout,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
    flingBehavior = flingBehavior,
    userScrollEnabled = userScrollEnabled,
    overscrollEffect = null,
    content = content,
)

/** [LazyVerticalGrid] without the stretch overscroll. */
@Composable
fun AppLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyGridScope.() -> Unit,
) = LazyVerticalGrid(
    columns = columns,
    modifier = modifier,
    state = state,
    contentPadding = contentPadding,
    reverseLayout = reverseLayout,
    verticalArrangement = verticalArrangement,
    horizontalArrangement = horizontalArrangement,
    flingBehavior = flingBehavior,
    userScrollEnabled = userScrollEnabled,
    overscrollEffect = null,
    content = content,
)

/** [verticalScroll] without the stretch overscroll. */
fun Modifier.appVerticalScroll(state: ScrollState): Modifier = verticalScroll(state, overscrollEffect = null)

/** [horizontalScroll] without the stretch overscroll. */
fun Modifier.appHorizontalScroll(state: ScrollState): Modifier = horizontalScroll(state, overscrollEffect = null)
