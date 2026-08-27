package com.greenrobotdev.linklibrary.design.demo

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import com.greenrobotdev.linklibrary.design.theme.MaterialExpressiveAppTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.components.button.FilledButton
import com.greenrobotdev.linklibrary.design.components.button.IconButton
import com.greenrobotdev.linklibrary.design.components.button.OutlinedButton
import com.greenrobotdev.linklibrary.design.components.button.TextButton
import com.greenrobotdev.linklibrary.design.components.card.ElevatedCard
import com.greenrobotdev.linklibrary.design.components.card.FilledCard
import com.greenrobotdev.linklibrary.design.components.card.OutlinedCard
import com.greenrobotdev.linklibrary.design.components.emptystate.EmptyState
import com.greenrobotdev.linklibrary.design.components.emptystate.CompactEmptyState
import com.greenrobotdev.linklibrary.design.components.progress.CircularProgress
import com.greenrobotdev.linklibrary.design.components.progress.DeterminateCircularProgress
import com.greenrobotdev.linklibrary.design.components.progress.DeterminateLinearProgress
import com.greenrobotdev.linklibrary.design.components.progress.LinearProgress
import com.greenrobotdev.linklibrary.design.components.progress.LinearProgressWithPercentage
import com.greenrobotdev.linklibrary.design.components.progress.CurlyCircularProgress
import com.greenrobotdev.linklibrary.design.components.progress.RoundedSquareProgress
import com.greenrobotdev.linklibrary.design.components.progress.SquircleProgress
import com.greenrobotdev.linklibrary.design.components.progress.DiamondProgress
import com.greenrobotdev.linklibrary.design.components.progress.StarProgress
import com.greenrobotdev.linklibrary.design.components.selection.AppCheckbox
import com.greenrobotdev.linklibrary.design.components.selection.AppRadio
import com.greenrobotdev.linklibrary.design.components.selection.AppRadioGroup
import com.greenrobotdev.linklibrary.design.components.selection.AppSlider
import com.greenrobotdev.linklibrary.design.components.selection.AppSwitch
import com.greenrobotdev.linklibrary.design.components.textfield.FilledTextField
import com.greenrobotdev.linklibrary.design.components.textfield.OutlinedTextField
import com.greenrobotdev.linklibrary.design.components.textfield.SearchField
import com.greenrobotdev.linklibrary.design.components.textfield.TextArea
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon
import com.greenrobotdev.linklibrary.design.foundation.icon.ErrorIcon
import com.greenrobotdev.linklibrary.design.foundation.layout.AppDivider
import com.greenrobotdev.linklibrary.design.foundation.layout.AppSpacer
import com.greenrobotdev.linklibrary.design.foundation.layout.HorizontalDivider
import com.greenrobotdev.linklibrary.design.foundation.layout.LargeVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.layout.MediumVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.layout.SmallVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.layout.TinyVerticalSpacer
import com.greenrobotdev.linklibrary.design.foundation.surface.AppCardSurface
import com.greenrobotdev.linklibrary.design.foundation.text.AppText

/**
 * Demo screen showcasing all implemented components from Phases 1-3
 * Use this to test and verify component behavior and appearance
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ComponentDemoScreen(onBack: () -> Unit = {}) {
    MaterialExpressiveAppTheme {
        var textFieldValue by remember { mutableStateOf("") }
        var searchQuery by remember { mutableStateOf("") }
        var textAreaValue by remember { mutableStateOf("") }
        var checkedState by remember { mutableStateOf(false) }
        var switchState by remember { mutableStateOf(true) }
        var sliderValue by remember { mutableStateOf(0.5f) }
        var selectedRadioOption by remember { mutableStateOf("Option 1") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
        AppText(
            text = "Component Library Demo",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
        )

        LargeVerticalSpacer()

        // Foundation Components Demo
        SectionTitle("Foundation Components")
        AppText(text = "AppText with default styling")
        AppText(text = "AppText with error styling", color = androidx.compose.material3.MaterialTheme.colorScheme.error)
        TinyVerticalSpacer()
        AppIcon(Icons.Default.Home, contentDescription = "Home")
        AppIcon(Icons.Default.Settings, contentDescription = "Settings")
        TinyVerticalSpacer()
        ErrorIcon()
        SmallVerticalSpacer()

        // Spacer Demo
        Row {
            AppText(text = "Before spacer")
            AppSpacer(size = 32.dp, horizontal = true)
            AppText(text = "After spacer")
        }

        MediumVerticalSpacer()

        // Divider Demo
        AppText(text = "Above divider")
        TinyVerticalSpacer()
        AppDivider()
        TinyVerticalSpacer()
        AppText(text = "Below divider")
        SmallVerticalSpacer()

        // Button Components Demo
        SectionTitle("Button Components")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledButton(
                text = "Primary",
                onClick = { },
                icon = Icons.Default.Add
            )
            OutlinedButton(
                text = "Secondary",
                onClick = { },
                icon = Icons.Default.Edit
            )
            TextButton(
                text = "Tertiary",
                onClick = { }
            )
        }

        SmallVerticalSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                icon = Icons.Default.Favorite,
                contentDescription = "Like",
                onClick = { }
            )
            IconButton(
                icon = Icons.Default.Share,
                contentDescription = "Share",
                onClick = { }
            )
            IconButton(
                icon = Icons.Default.Delete,
                contentDescription = "Delete",
                onClick = { },
                tint = androidx.compose.material3.MaterialTheme.colorScheme.error
            )
        }

        MediumVerticalSpacer()

        // Text Field Components Demo
        SectionTitle("Text Field Components")

        FilledTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = "Filled Input",
            placeholder = "Enter text...",
            leadingIcon = Icons.Default.Email
        )

        SmallVerticalSpacer()

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = "Outlined Input",
            placeholder = "Enter text...",
            isError = false
        )

        SmallVerticalSpacer()

        SearchField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search..."
        )

        SmallVerticalSpacer()

        TextArea(
            value = textAreaValue,
            onValueChange = { textAreaValue = it },
            label = "Description",
            placeholder = "Enter description...",
            minLines = 3,
            maxLines = 5
        )

        MediumVerticalSpacer()

        // Selection Controls Demo
        SectionTitle("Selection Controls")

        AppCheckbox(
            checked = checkedState,
            onCheckedChange = { checkedState = it },
            label = "Accept terms and conditions"
        )

        SmallVerticalSpacer()

        AppSwitch(
            checked = switchState,
            onCheckedChange = { switchState = it },
            label = "Enable notifications"
        )

        SmallVerticalSpacer()

        AppRadioGroup(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOption = selectedRadioOption,
            onOptionSelected = { selectedRadioOption = it }
        )

        SmallVerticalSpacer()

        AppSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            showValue = true
        )

        SmallVerticalSpacer()

        AppSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            showValue = true,
            valueRange = 0f..100f,
            steps = 10
        )

        MediumVerticalSpacer()

        // Card Components Demo
        SectionTitle("Card Components")

        ElevatedCard {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText(
                    text = "Elevated Card",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                SmallVerticalSpacer()
                AppText(
                    text = "This is an elevated card with default styling.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }

        SmallVerticalSpacer()

        FilledCard {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText(
                    text = "Filled Card",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                SmallVerticalSpacer()
                AppText(
                    text = "This is a filled card with higher emphasis.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }

        SmallVerticalSpacer()

        OutlinedCard {
            Column(modifier = Modifier.padding(16.dp)) {
                AppText(
                    text = "Outlined Card",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                SmallVerticalSpacer()
                AppText(
                    text = "This is an outlined card with minimal emphasis.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }

        MediumVerticalSpacer()

        // Progress Indicators Demo
        SectionTitle("Progress Indicators")

        AppText(text = "Standard Progress")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgress()
            CircularProgress()
        }

        SmallVerticalSpacer()

        AppText(text = "Determinate Progress (75%)")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeterminateLinearProgress(progress = 0.75f)
            DeterminateCircularProgress(progress = 0.75f)
        }

        SmallVerticalSpacer()

        AppText(text = "Progress with Percentage")
        LinearProgressWithPercentage(progress = 0.42f, showPercentage = true)

        MediumVerticalSpacer()

        // Expressive Progress Indicators Demo
        SectionTitle("Expressive Progress - Curly & Shaped")

        AppText(text = "Curly Progress")
        CurlyCircularProgress()

        SmallVerticalSpacer()

        AppText(text = "Shaped Progress Indicators")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RoundedSquareProgress()
            SquircleProgress()
            DiamondProgress()
        }

        SmallVerticalSpacer()

        AppText(text = "Star Progress")
        StarProgress()

        MediumVerticalSpacer()

        // Empty States Demo
        SectionTitle("Empty States")

        EmptyState(
            icon = Icons.Default.Home,
            title = "No items found",
            description = "Get started by adding your first item to see it here.",
            actionLabel = "Add Item",
            onActionClick = { }
        )

        MediumVerticalSpacer()

        CompactEmptyState(
            icon = Icons.Default.Search,
            title = "No results",
            description = "Try a different search term"
        )

        LargeVerticalSpacer()

        // Theme and Colors Demo
        SectionTitle("Theme Integration")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppCardSurface(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AppText(
                        text = "Interactive",
                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                    )
                    SmallVerticalSpacer()
                    AppText(
                        text = "Card with click handler",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }

            AppCardSurface(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AppText(
                        text = "Static",
                        style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                    )
                    SmallVerticalSpacer()
                    AppText(
                        text = "Card without click",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        LargeVerticalSpacer()
        HorizontalDivider()
        LargeVerticalSpacer()

        AppText(
            text = "Component Library Demo Complete!",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        LargeVerticalSpacer()

        // Component Count
        val componentCount = 31
        AppText(
            text = "$componentCount production-ready components implemented",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    AppText(
        text = title,
        style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    AppDivider()
    TinyVerticalSpacer()
}