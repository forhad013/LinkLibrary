package com.greenrobotdev.lordofthelinks.database.repository

import com.greenrobotdev.lordofthelinks.database.room.CategoryDao
import com.greenrobotdev.lordofthelinks.model.Category
import com.greenrobotdev.lordofthelinks.model.SmartListCounts
import com.greenrobotdev.lordofthelinks.model.repository.CategoryRepository
import com.greenrobotdev.lordofthelinks.database.room.LinkCategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomCategoryRepository(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override suspend fun getCategories(): Flow<Result<List<Category>>> =
        categoryDao.getAllCategories()
            .map { rows -> Result.success(rows.map { it.toCategory() }) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getCategoryById(id: String): Flow<Result<Category?>> =
        categoryDao.getCategoryById(id)
            .map { Result.success(it?.toCategory()) }
            .catch { emit(Result.failure(it)) }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addCategory(category: Category): Flow<Result<Category>> = flow {
        try {
            val entity = category.toEntity()
            val toSave = if (entity.id.isEmpty()) entity.copy(id = Uuid.random().toString()) else entity
            categoryDao.insertCategory(toSave)
            emit(Result.success(toSave.toCategory()))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
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
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun moveCategory(categoryId: String, newParentId: String?): Flow<Result<Unit>> = flow {
        try {
            categoryDao.moveCategory(categoryId, newParentId)
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteCategory(categoryId: String): Flow<Result<Unit>> = flow {
        try {
            categoryDao.deleteCategoryLeafOnly(categoryId)
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteCategoryKeepChildren(categoryId: String): Flow<Result<Unit>> = flow {
        try {
            val category = categoryDao.getCategoryByIdOnce(categoryId)
                ?: return@flow emit(Result.failure(Exception("Category not found: $categoryId")))
            categoryDao.moveChildrenTo(categoryId, category.parentId)
            categoryDao.deleteCategoryById(categoryId)
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun deleteCategoryTree(categoryId: String): Flow<Result<Unit>> = flow {
        try {
            // categories.parentId FK CASCADE removes descendants; link_categories CASCADE uncategorizes their links
            categoryDao.deleteCategoryById(categoryId)
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }


    override suspend fun getCategoryForLink(linkId: String): Flow<Result<Category?>> = flow {
        try {
            emit(Result.success(categoryDao.getCategoryForLink(linkId)?.toCategory()))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun assignLinkToCategory(linkId: String, categoryId: String): Flow<Result<Unit>> = flow {
        try {
            categoryDao.assignLinkToCategory(LinkCategoryEntity(linkId = linkId, categoryId = categoryId))
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun clearLinkCategory(linkId: String): Flow<Result<Unit>> = flow {
        try {
            categoryDao.clearLinkCategory(linkId)
            emit(Result.success(Unit))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getLinkCategoryIds(): Flow<Result<Map<String, String>>> =
        categoryDao.getAllLinkCategories()
            .map { rows -> Result.success(rows.associate { it.linkId to it.categoryId }) }
            .catch { e -> emit(Result.failure(e)) }

    override suspend fun getRecentCategories(): Flow<Result<List<Category>>> = flow {
        try {
            emit(Result.success(categoryDao.getRecentCategories().map { it.toCategory() }))
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            // cancellation (incl. first() AbortFlow) must propagate, not become Result.failure
            throw e
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getCategoriesWithCounts(): Flow<Result<List<Category>>> =
        categoryDao.getAllCategoriesWithCounts()
            .map { rows -> Result.success(rows.map { it.toCategory() }) }
            .catch { emit(Result.failure(it)) }

    override suspend fun getSmartListCounts(): Flow<Result<SmartListCounts>> =
        categoryDao.getSmartListCounts()
            .map { Result.success(it.toDomain()) }
            .catch { emit(Result.failure(it)) }
}
