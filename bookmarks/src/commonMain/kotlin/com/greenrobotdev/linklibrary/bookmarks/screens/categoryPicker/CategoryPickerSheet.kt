package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.layout.BaseBottomSheet

/**
 * Category picker as a modal bottom sheet (GRE-44).
 * Close/swipe/back leaves the link's category untouched; "Use this" applies it.
 */
@Composable
fun CategoryPickerSheet(
    onDismiss: () -> Unit,
    linkId: String? = null,
    initialSelection: String? = null,
    onApplied: (categoryId: String?) -> Unit = {},
) {
    val viewModel = remember { CategoryPickerViewModel(linkId, initialSelection) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.applied) {
        if (state.applied) {
            onApplied(state.selectedCategoryId)
            onDismiss()
        }
    }

    BaseBottomSheet(
        title = "Category",
        onClose = onDismiss,
        closeContentDescription = "Close category picker",
    ) {
        CategoryPickerContent(
            state = state,
            onEvent = viewModel::take,
            onBack = onDismiss,
            modifier = Modifier.fillMaxWidth().weight(1f),
        )
    }
}

/** Full-screen host for the same picker UI (Organize tab's "move category" entry). */
@Composable
fun CategoryPickerScreen(
    initialSelection: String? = null,
    onDone: (categoryId: String?) -> Unit,
    onBack: () -> Unit = {},
) {
    val viewModel = remember { CategoryPickerViewModel(linkId = null, initialSelection) }
    val state by viewModel.models.collectAsState()

    LaunchedEffect(state.applied) {
        if (state.applied) onDone(state.selectedCategoryId)
    }

    CategoryPickerContent(
        state = state,
        onEvent = viewModel::take,
        onBack = onBack,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    )
}
