package com.greenrobotdev.linklibrary.app.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.bookmarks.model.Link
import com.greenrobotdev.linklibrary.bookmarks.model.Tag
import com.greenrobotdev.linklibrary.bookmarks.screens.library.ModernLinkCard
import com.greenrobotdev.linklibrary.design.theme.AppTheme
import com.greenrobotdev.linklibrary.design.components.advanced.TagChipsSelector
import com.greenrobotdev.linklibrary.design.components.advanced.TagChip
import com.greenrobotdev.linklibrary.design.components.advanced.CollectionDropdown
import com.greenrobotdev.linklibrary.design.components.advanced.AutoCompleteDropdown
import com.greenrobotdev.linklibrary.design.components.advanced.AutoFetchButton
import com.greenrobotdev.linklibrary.design.components.layout.BentoGrid
import com.greenrobotdev.linklibrary.design.components.layout.BulkOperationsBar
import com.greenrobotdev.linklibrary.design.components.cards.LinkCard
import com.greenrobotdev.linklibrary.design.components.cards.LinkCardGridItem
import com.greenrobotdev.linklibrary.design.components.cards.LinkCardListItem
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Custom Component Previews
 *
 * Previews for all custom Material 3 Expressive components
 */
@OptIn(ExperimentalUuidApi::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LinkCardsPreview() {
    val sampleLinks = listOf(
        Link(
            id = Uuid.random().toString(),
            url = "https://example.com/article1",
            title = "Introduction to Material 3 Design",
            description = "Learn about the latest Material Design principles and components",
            tags = listOf("Design", "UI/UX"),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        Link(
            id = Uuid.random().toString(),
            url = "https://example.com/article2",
            title = "Kotlin Multiplatform Guide",
            description = "Complete guide to building cross-platform apps with KMP",
            tags = listOf("Kotlin", "Development"),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        Link(
            id = Uuid.random().toString(),
            url = "https://example.com/article3",
            title = "Jetpack Compose Tutorial",
            description = "Master modern Android UI development with Compose",
            tags = listOf("Android", "Compose", "Tutorial"),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Link Cards",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Standard LinkCard
                LinkCard(
                    title = sampleLinks[0].title,
                    url = sampleLinks[0].url,
                    description = sampleLinks[0].description,
                    tags = sampleLinks[0].tags,
                    isFavorite = true,
                    isBookmarked = true,
                    onClick = {},
                    onFavoriteClick = {},
                    onBookmarkClick = {}
                )

                // Grid Item Variant
                LinkCardGridItem(
                    title = sampleLinks[1].title,
                    url = sampleLinks[1].url,
                    description = sampleLinks[1].description,
                    tags = sampleLinks[1].tags,
                    onClick = {}
                )

                // List Item Variant
                LinkCardListItem(
                    title = sampleLinks[2].title,
                    url = sampleLinks[2].url,
                    description = sampleLinks[2].description,
                    tags = sampleLinks[2].tags,
                    isFavorite = false,
                    isBookmarked = true,
                    onClick = {},
                    onFavoriteClick = {},
                    onBookmarkClick = {}
                )

                // Modern Library Card
                Text(
                    text = "Modern Library Card",
                    style = MaterialTheme.typography.titleMedium
                )
                ModernLinkCard(
                    link = sampleLinks[0],
                    onClick = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview(showBackground = true, widthDp = 360, heightDp = 600)
@Composable
fun TagAndCollectionComponentsPreview() {
    val sampleTags = listOf(
        Tag(id = "1", name = "Design", color = 0xFF4C22BD),
        Tag(id = "2", name = "Development", color = 0xFF6349BF),
        Tag(id = "3", name = "UI/UX", color = 0xFF9A67EA),
        Tag(id = "4", name = "Tutorial", color = 0xFFD0BCFF),
        Tag(id = "5", name = "Android", color = 0xFFEADDFF),
        Tag(id = "6", name = "Compose", color = 0xFF7F67BE)
    )

    val sampleCollections = listOf(
        "Development Resources",
        "Design Inspiration",
        "Learning Materials",
        "Tools & Libraries"
    )

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Tag & Collection Components",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Tag Chips Selector
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tag Chips Selector",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TagChipsSelector(
                            availableTags = sampleTags,
                            selectedTags = mutableListOf(sampleTags[0], sampleTags[2]),
                            onTagsChanged = {}
                        )
                    }
                }

                // Individual Tag Chips
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Individual Tag Chips",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sampleTags.take(4).forEach { tag ->
                                TagChip(
                                    tag = tag.name,
                                    selected = false,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }

                // Collection Dropdown
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Collection Dropdown",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CollectionDropdown(
                            collections = sampleCollections,
                            selectedCollection = sampleCollections[0],
                            onCollectionSelected = {},
                            onCreateNew = {}
                        )
                    }
                }

                // Auto-complete Dropdown
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Auto-complete Dropdown",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AutoCompleteDropdown(
                            label = "Select or create collection",
                            items = sampleCollections,
                            selectedItem = sampleCollections[0],
                            onItemSelected = {},
                            onCreateItem = {}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun AdvancedComponentsPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Advanced Components",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Auto-Fetch Button
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Auto-Fetch Button",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AutoFetchButton(
                            url = "https://example.com/article",
                            isFetching = false,
                            onFetch = {}
                        )
                    }
                }

                // Bulk Operations Bar
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bulk Operations Bar",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BulkOperationsBar(
                            selectedCount = 3,
                            totalCount = 10,
                            onClearSelection = {},
                            onDelete = {},
                            onMoveToCollection = {},
                            onAddTags = {}
                        )
                    }
                }

                // Bento Grid Preview
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bento Grid Layout",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        BentoGrid {
                            // Sample bento grid content would go here
                            Text(
                                text = "Bento grid with responsive layout\nand expressive animations",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkModePreview() {
    val sampleLinks = listOf(
        Link(
            id = "1",
            url = "https://example.com",
            title = "Dark Mode Link Card",
            description = "Testing theme colors in dark mode",
            tags = listOf("Theme", "Dark Mode"),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )

    AppTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Dark Mode Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                sampleLinks.forEach { link ->
                    LinkCard(
                        title = link.title,
                        url = link.url,
                        description = link.description,
                        tags = link.tags,
                        onClick = {}
                    )
                }

                ModernLinkCard(
                    link = sampleLinks[0],
                    onClick = {}
                )
            }
        }
    }
}
