package com.greenrobotdev.linklibrary.bookmarks.screens.tags.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsViewModel
import com.greenrobotdev.linklibrary.bookmarks.screens.tags.TagsScreenState

/**
 * TagsView - Main view component with screen-level logic and state management
 *
 * This component handles:
 * - View model initialization and state management
 * - FAB animation states
 * - Content layout orchestration
 */
@Composable
fun TagsView(
    routeKey: NavKey,
    onCreateTag: () -> Unit,
    onManageTags: () -> Unit,
    content: @Composable (
        state: TagsScreenState,
        onCreateTag: () -> Unit,
        onManageTags: () -> Unit,
        modifier: Modifier
    ) -> Unit
) {
    val viewModel: TagsViewModel = viewModel<TagsViewModel>(key = routeKey.toString()) { TagsViewModel() }
    val state by viewModel.models.collectAsState()

    // Content callback with all necessary state and callbacks
    content(
        state,
        onCreateTag,
        onManageTags,
        Modifier
    )
}

/**
 * TagsScreenScaffold - Scaffold with top bar and FAB
 *
 * Provides the standard Material 3 layout for the tags screen
 */
@Composable
fun TagsScreenScaffold(
    onCreateTag: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    // FAB animation
    val fabScale by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(1f)
    }

    val animatedFabScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = fabScale,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
        ),
        label = "fab_scale"
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TagsTopBar()
        },
        floatingActionButton = {
            TagsFloatingActionButton(
                onClick = onCreateTag,
                scale = animatedFabScale
            )
        }
    ) { padding ->
        content(padding)
    }
}

/**
 * TagsFloatingActionButton - Animated FAB for tag creation
 */
@Composable
private fun TagsFloatingActionButton(
    onClick: () -> Unit,
    scale: Float
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Create new tag",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}