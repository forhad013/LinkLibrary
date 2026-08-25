package com.greenrobotdev.linklibrary.bookmarks.screens.add

// INTEGRATION GUIDE FOR AddLinkScreen.kt
// This file contains example code for integrating AI features into AddLinkScreen
// DO NOT use this file directly - integrate these components into AddLinkScreen.kt as needed

// 1. ADD THESE TO YOUR STATE
// Update AddLinkState in AddLinkStateModels.kt:
/*
@Serializable
data class AddLinkState(
    // ... existing fields ...
    val tags: List<String> = emptyList(),  // ADD THIS
    val suggestedTags: List<String> = emptyList(),  // ADD THIS
    val isGeneratingTags: Boolean = false,  // ADD THIS
    val isGeneratingDescription: Boolean = false  // ADD THIS
)
*/

// 2. ADD THESE TO YOUR EVENTS
// Update AddLinkEvent in AddLinkStateModels.kt:
/*
sealed interface AddLinkEvent {
    // ... existing events ...
    data class TagsChanged(val tags: List<String>) : AddLinkEvent  // ADD THIS
    data class SuggestedTagsChanged(val tags: List<String>) : AddLinkEvent  // ADD THIS
    data class GenerateTags(val url: String, val title: String) : AddLinkEvent  // ADD THIS
    data class GenerateDescription(val url: String, val title: String) : AddLinkEvent  // ADD THIS
    data class SelectTag(val tag: String) : AddLinkEvent  // ADD THIS
    data class DeselectTag(val tag: String) : AddLinkEvent  // ADD THIS
}
*/

// 3. EXAMPLE USAGE IN AddLinkScreen.kt
/*
// In your AddLinkScreen composable, after the description field:

// AI Description Generator
AIDescriptionGenerator(
    url = state.url,
    title = state.title,
    currentDescription = state.description,
    onGenerateDescription = {
        viewModel.take(AddLinkEvent.GenerateDescription(state.url, state.title))
    },
    isGenerating = state.isGeneratingDescription
)

// AI Tag Suggestions
AITagSuggestionSection(
    url = state.url,
    title = state.title,
    suggestedTags = state.suggestedTags,
    isGenerating = state.isGeneratingTags,
    onGenerateTags = {
        viewModel.take(AddLinkEvent.GenerateTags(state.url, state.title))
    },
    onTagToggle = { tag ->
        if (state.tags.contains(tag)) {
            viewModel.take(AddLinkEvent.DeselectTag(tag))
        } else {
            viewModel.take(AddLinkEvent.SelectTag(tag))
        }
    },
    onTagsSelected = { tags ->
        viewModel.take(AddLinkEvent.TagsChanged(tags))
    }
)
*/

// 4. EXAMPLE FIELD INTEGRATION
/*
// After your URL and Title fields, add the AI components:

// URL Field (existing)
OutlinedTextField(
    value = state.url,
    onValueChange = { viewModel.take(AddLinkEvent.UrlChanged(it)) },
    label = { Text("URL") },
    leadingIcon = { Icon(Icons.Default.Link, contentDescription = null) },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true,
    isError = state.error != null,
    enabled = !state.isLoading
)

// Title Field (existing)
OutlinedTextField(
    value = state.title,
    onValueChange = { viewModel.take(AddLinkEvent.TitleChanged(it)) },
    label = { Text("Title (optional)") },
    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true,
    enabled = !state.isLoading
)

// ========== AI DESCRIPTION GENERATOR (ADD THIS) ==========
AIDescriptionGenerator(
    url = state.url,
    title = state.title,
    currentDescription = state.description,
    onGenerateDescription = {
        viewModel.take(AddLinkEvent.GenerateDescription(state.url, state.title))
    },
    isGenerating = state.isGeneratingDescription
)

// Description Field (existing)
OutlinedTextField(
    value = state.description,
    onValueChange = { viewModel.take(AddLinkEvent.DescriptionChanged(it)) },
    label = { Text("Description (optional)") },
    modifier = Modifier.fillMaxWidth()
                    .height(120.dp),
                enabled = !state.isLoading
            )

// ========== AI TAG SUGGESTIONS (ADD THIS) ==========
            AITagSuggestionSection(
                url = state.url,
                title = state.title,
                suggestedTags = state.suggestedTags,
                isGenerating = state.isGeneratingTags,
                onGenerateTags = {
                    viewModel.take(AddLinkEvent.GenerateTags(state.url, state.title))
                },
                onTagToggle = { tag ->
                    if (state.tags.contains(tag)) {
                        viewModel.take(AddLinkEvent.DeselectTag(tag))
                    } else {
                        viewModel.take(AddLinkEvent.SelectTag(tag))
                    }
                },
                onTagsSelected = { tags ->
                    viewModel.take(AddLinkEvent.TagsChanged(tags))
                }
            )

// ========== DUPLICATE DETECTION (OPTIONAL - ADD THIS) ==========
            if (state.url.isNotBlank()) {
                DuplicateDetectionWarning(
                    url = state.url,
                    existingLinks = emptyList(), // TODO: Pass your actual links here from ViewModel
                    onContinueAnyway = {
                        // User chose to add anyway - no action needed
                    }
                )
            }

// ... rest of your existing fields and buttons
*/
