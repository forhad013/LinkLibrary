package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.CategoryEntity
import com.greenrobotdev.linklibrary.database.room.CategoryWithCount
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories(): Flow<Result<List<CategoryEntity>>>
    suspend fun getCategoryById(id: String): Flow<Result<CategoryEntity?>>
    suspend fun addCategory(category: CategoryEntity): Flow<Result<CategoryEntity>>
    suspend fun renameCategory(categoryId: String, newName: String): Flow<Result<Unit>>
    suspend fun moveCategory(categoryId: String, newParentId: String?): Flow<Result<Unit>>
    suspend fun deleteCategory(categoryId: String): Flow<Result<Unit>>
    suspend fun getCategoryForLink(linkId: String): Flow<Result<CategoryEntity?>>
    suspend fun assignLinkToCategory(linkId: String, categoryId: String): Flow<Result<Unit>>
    suspend fun getCategoriesWithCounts(): Flow<Result<List<CategoryWithCount>>>
}
