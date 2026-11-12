package com.greenrobotdev.linklibrary.database.repository

import com.greenrobotdev.linklibrary.database.room.CategoryDao
import com.greenrobotdev.linklibrary.database.room.CategoryEntity
import com.greenrobotdev.linklibrary.database.room.CategoryWithCount
import com.greenrobotdev.linklibrary.database.room.LinkCategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomCategoryRepository(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun getCategories(): Flow<Result<List<CategoryEntity>>> =
        categoryDao.getAllCategories()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getCategoryById(id: String): Flow<Result<CategoryEntity?>> =
        categoryDao.getCategoryById(id)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addCategory(category: CategoryEntity): Flow<Result<CategoryEntity>> = flow {
        try {
            val toSave = if (category.id.isEmpty()) category.copy(id = Uuid.random().toString()) else category
            categoryDao.insertCategory(toSave)
            emit(Result.success(toSave))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun renameCategory(categoryId: String, newName: String): Flow<Result<Unit>> = flow {
        try {
            val category = categoryDao.getCategoryByIdOnce(categoryId)
                ?: return@flow emit(Result.failure(Exception("Category not found: $categoryId")))
            categoryDao.updateCategory(category.copy(name = newName, updatedAt = System.currentTimeMillis()))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun moveCategory(categoryId: String, newParentId: String?): Flow<Result<Unit>> = flow {
        try {
            categoryDao.moveCategory(categoryId, newParentId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteCategory(categoryId: String): Flow<Result<Unit>> = flow {
        try {
            categoryDao.deleteCategoryById(categoryId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getCategoryForLink(linkId: String): Flow<Result<CategoryEntity?>> = flow {
        try {
            emit(Result.success(categoryDao.getCategoryForLink(linkId)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun assignLinkToCategory(linkId: String, categoryId: String): Flow<Result<Unit>> = flow {
        try {
            categoryDao.assignLinkToCategory(LinkCategoryEntity(linkId = linkId, categoryId = categoryId))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getCategoriesWithCounts(): Flow<Result<List<CategoryWithCount>>> =
        categoryDao.getAllCategoriesWithCounts()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getSmartListCounts(): Flow<Result<CategoryDao.SmartListCounts>> =
        categoryDao.getSmartListCounts()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
