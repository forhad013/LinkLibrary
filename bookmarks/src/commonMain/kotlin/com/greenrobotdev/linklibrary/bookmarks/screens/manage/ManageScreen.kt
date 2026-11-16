package com.greenrobotdev.linklibrary.bookmarks.screens.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryPickerContent
import com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker.CategoryPickerViewModel
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerContent
import com.greenrobotdev.linklibrary.bookmarks.screens.tagPicker.TagPickerViewModel

/**
 * Manage categories and tags. Both tabs embed the matching picker content inline
 * (create / rename / delete with undo), without the confirm pill.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageScreen(onBack: () -> Unit) {
    var showCategories by rememberSaveable { mutableStateOf(true) }
    val pickerViewModel = remember { CategoryPickerViewModel(linkId = null) }
    val pickerState by pickerViewModel.models.collectAsState()
    val tagViewModel = remember { TagPickerViewModel(multiSelect = false) }
    val tagState by tagViewModel.models.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .imePadding()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            // Segmented switcher: white track, selected segment uses the Library pill colors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TogglePill(
                    "Categories",
                    active = showCategories,
                    modifier = Modifier.weight(1f),
                    onClick = { showCategories = true }
                )
                TogglePill(
                    "Tags",
                    active = !showCategories,
                    modifier = Modifier.weight(1f),
                    onClick = { showCategories = false }
                )
            }

            if (showCategories) {
                CategoryPickerContent(
                    state = pickerState,
                    onEvent = pickerViewModel::take,
                    onBack = onBack,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    showUseThis = false,
                )
            } else {
                TagPickerContent(
                    state = tagState,
                    onEvent = tagViewModel::take,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    multiSelect = false,
                )
            }
        }
    }
}

@Composable
private fun TogglePill(label: String, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (active) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .selectable(selected = active, onClick = onClick, role = Role.Tab)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
