package com.greenrobotdev.linklibrary.bookmarks.screens.categoryPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.greenrobotdev.linklibrary.bookmarks.model.Category
import com.greenrobotdev.linklibrary.bookmarks.model.toCategory
import com.greenrobotdev.linklibrary.bookmarks.model.toEntity
import com.greenrobotdev.linklibrary.database.repository.CategoryRepository
import com.greenrobotdev.linklibrary.utils.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CategoryPickerViewModel : MoleculeViewModel<CategoryPickerEvent, CategoryPickerState>(), KoinComponent {

    private val categoryRepository: CategoryRepository by inject()

    @Composable
    override fun models(events: Flow<CategoryPickerEvent>): CategoryPickerState {
        var state by remember { mutableStateOf(CategoryPickerState()) }

        LaunchedEffect(Unit) {
            categoryRepository.getCategoriesWithCounts().collect { result ->
                result.fold(
                    onSuccess = { rows ->
                        state = state.copy(
                            isLoading = false,
                            categories = rows.map { it.toCategory() }
                        )
                    },
                    onFailure = { state = state.copy(isLoading = false, error = it.message) }
                )
            }
        }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is CategoryPickerEvent.Select ->
                        state = state.copy(selectedCategoryId = event.categoryId)
                    is CategoryPickerEvent.CreateCategory -> {
                        val entity = Category(id = "", name = event.name, parentId = event.parentId).toEntity()
                        categoryRepository.addCategory(entity).first().fold(
                            onSuccess = { },
                            onFailure = { state = state.copy(error = it.message ?: "Could not create category") }
                        )
                    }
                    CategoryPickerEvent.ClearError -> state = state.copy(error = null)
                }
            }
        }

        return state
    }

    override fun initialValue(): CategoryPickerState = CategoryPickerState()
}
