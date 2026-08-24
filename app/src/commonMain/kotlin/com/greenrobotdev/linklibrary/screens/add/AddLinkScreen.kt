package com.greenrobotdev.linklibrary.screens.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.greenrobotdev.linklibrary.components.form.AutoFetchButton
import com.greenrobotdev.linklibrary.screens.add.CompactTagCollectionSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkScreen(
    routeKey: NavKey,
    initialUrl: String?,
    onBack: () -> Unit,
    onAddTag: () -> Unit = {},
    onAddCollection: () -> Unit = {}
) {

    val viewModel: AddLinkViewModel = viewModel<AddLinkViewModel>(key = routeKey.toString()) { AddLinkViewModel(initialUrl) }

    val state by viewModel.states.collectAsState()

    // Navigate back after successful link addition
    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Link") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // URL Field with Auto-fetch
            Column {
                OutlinedTextField(
                    value = state.url,
                    onValueChange = { viewModel.onEvent(AddLinkEvent.UrlChanged(it)) },
                    label = { Text("URL") },
                    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = state.error != null,
                    enabled = !state.isLoading
                )

                // Auto-fetch button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    AutoFetchButton(
                        isFetching = state.isFetching,
                        onClick = { viewModel.onEvent(AddLinkEvent.FetchMetadata) },
                        enabled = state.url.isNotBlank() && !state.isLoading
                    )
                }

                // Fetch error message
                state.fetchError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Title Field
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onEvent(AddLinkEvent.TitleChanged(it)) },
                label = { Text("Title (optional)") },
                leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !state.isLoading
            )

            // Description Field
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AddLinkEvent.DescriptionChanged(it)) },
                label = { Text("Description (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                enabled = !state.isLoading
            )

            // Tag and Collection Selector
            CompactTagCollectionSelector(
                tags = state.availableTags,
                collections = state.availableCollections,
                selectedTags = state.selectedTags,
                selectedCollections = state.selectedCollections,
                onTagToggle = { tagId -> viewModel.onEvent(AddLinkEvent.ToggleTag(tagId)) },
                onCollectionToggle = { collectionId -> viewModel.onEvent(AddLinkEvent.ToggleCollection(collectionId)) },
                isLoading = state.isLoadingTagsAndCollections,
                onAddTag = onAddTag,
                onAddCollection = onAddCollection
            )

            // Favorite Toggle
            TextButton(
                onClick = {
                    viewModel.onEvent(AddLinkEvent.ToggleFavorite(!state.isFavorite))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading || !state.isFormValid
            ) {
                Icon(
                    imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (state.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Text("Add to favorites")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Submit Button
            Button(
                onClick = { viewModel.onEvent(AddLinkEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                enabled = state.isFormValid && !state.isLoading
            ) {
                if (state.isLoading) {
                    Text("Saving...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Save Link")
                }
            }

            // Error message
            state.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
