package com.greenrobotdev.lordofthelinks.model.repository

import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.SmartListCounts
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<Result<List<Category>>>
    suspend fun getCategoryById(id: String): Flow<Result<Category?>>
    suspend fun addCategory(category: Category): Flow<Result<Category>>
    suspend fun renameCategory(categoryId: String, newName: String): Flow<Result<Unit>>
    suspend fun moveCategory(categoryId: String, newParentId: String?): Flow<Result<Unit>>
    suspend fun deleteCategory(categoryId: String): Flow<Result<Unit>>
    /** Delete a category keeping its subtree: children are reparented to the deleted category's parent. */
    suspend fun deleteCategoryKeepChildren(categoryId: String): Flow<Result<Unit>>
    /** Delete a category and every descendant (links become uncategorized). */
    suspend fun deleteCategoryTree(categoryId: String): Flow<Result<Unit>>
    suspend fun getCategoryForLink(linkId: String): Flow<Result<Category?>>
    suspend fun assignLinkToCategory(linkId: String, categoryId: String): Flow<Result<Unit>>
    suspend fun clearLinkCategory(linkId: String): Flow<Result<Unit>>
    suspend fun getRecentCategories(): Flow<Result<List<Category>>>
    /** linkId → categoryId for every categorized link; re-emits on change. */
    fun getLinkCategoryIds(): Flow<Result<Map<String, String>>>
    suspend fun getCategoriesWithCounts(): Flow<Result<List<Category>>>
    suspend fun getSmartListCounts(): Flow<Result<SmartListCounts>>
}
