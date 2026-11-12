package com.greenrobotdev.linklibrary.bookmarks.screens.addCategory

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.greenrobotdev.linklibrary.design.components.button.AppButton
import com.greenrobotdev.linklibrary.design.components.card.AppErrorCard
import com.greenrobotdev.linklibrary.design.components.card.AppIntroductionCard
import com.greenrobotdev.linklibrary.design.components.textfield.AppTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    routeKey: NavKey,
    onBack: () -> Unit
) {
    val viewModel: AddCategoryViewModel = viewModel(key = routeKey.toString()) {
        AddCategoryViewModel()
    }
    val state by viewModel.models.collectAsState()

    // Navigate back after successful category creation
    LaunchedEffect(state.success) {
        if (state.success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Category") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Introduction Card
            AppIntroductionCard(
                title = "Create New Category",
                description = "Organize your links into categoryNames to easily find them later",
                modifier = Modifier.fillMaxWidth()
            )

            // Category Name Field
            AppTextField(
                value = state.name,
                onValueChange = { viewModel.take(AddCategoryEvent.NameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = "Category Name",
                placeholder = "e.g., AI & Tech, Design Resources",
                singleLine = true,
                isError = state.error != null,
                enabled = !state.isLoading
            )

            // Description Field
            AppTextField(
                value = state.description,
                onValueChange = { viewModel.take(AddCategoryEvent.DescriptionChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = "Description",
                placeholder = "What type of links will this category contain?",
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.weight(1f))

            // Submit Button
            AppButton(
                text = "Create Category",
                onClick = { viewModel.take(AddCategoryEvent.Submit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormValid,
                isLoading = state.isLoading
            )

            // Error message
            state.error?.let { error ->
                AppErrorCard(
                    error = error,
                    modifier = Modifier.fillMaxWidth(),
                    onDismiss = { viewModel.take(AddCategoryEvent.ClearError) }
                )
            }
        }
    }
}
