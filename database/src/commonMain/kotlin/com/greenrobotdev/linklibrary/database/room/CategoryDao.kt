package com.greenrobotdev.linklibrary.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

private const val MAX_DEPTH = 5

class MaxDepthExceededException(depth: Int) :
    Exception("Category nesting exceeds max depth $MAX_DEPTH (attempted depth $depth)")

/**
 * Data Access Object for nested Category entities.
 * Depth limit (5) enforced on insert and move; depth of a node = ancestors + 1.
 */
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryById(categoryId: String): Flow<CategoryEntity?>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryByIdOnce(categoryId: String): CategoryEntity?

    @Query("SELECT * FROM categories WHERE parentId = :parentId ORDER BY name ASC")
    fun getChildren(parentId: String): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryRaw(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: String)

    /**
     * Insert, rejecting if the resulting depth would exceed MAX_DEPTH.
     */
    @Transaction
    suspend fun insertCategory(category: CategoryEntity) {
        val depth = depthOf(category.parentId) + 1
        if (depth > MAX_DEPTH) throw MaxDepthExceededException(depth)
        insertCategoryRaw(category)
    }

    /**
     * Move a category under a new parent, rejecting if the subtree would exceed MAX_DEPTH
     * or would create a cycle (moving under own descendant).
     */
    @Transaction
    suspend fun moveCategory(categoryId: String, newParentId: String?) {
        if (categoryId == newParentId) throw Exception("Cannot move a category under itself")
        var node = newParentId?.let { getCategoryByIdOnce(it) }
        while (node != null) {
            if (node.id == categoryId) throw Exception("Cannot move a category under its own descendant")
            node = node.parentId?.let { getCategoryByIdOnce(it) }
        }
        val subtreeDepth = maxSubtreeDepth(categoryId)
        val newDepth = depthOf(newParentId) + 1 + subtreeDepth - 1
        if (newDepth > MAX_DEPTH) throw MaxDepthExceededException(newDepth)
        val category = getCategoryByIdOnce(categoryId) ?: throw Exception("Category not found: $categoryId")
        updateCategory(category.copy(parentId = newParentId, updatedAt = System.currentTimeMillis()))
    }

    /** Depth of a category (root = 1). Null parent id → 0. */
    suspend fun depthOf(parentId: String?): Int {
        var depth = 0
        var currentId: String? = parentId
        while (currentId != null) {
            depth++
            currentId = getCategoryByIdOnce(currentId)?.parentId
        }
        return depth
    }

    /** Deepest chain within the subtree rooted at categoryId (root alone = 1). */
    suspend fun maxSubtreeDepth(categoryId: String): Int = walkDepth(categoryId, 1)

    suspend fun walkDepth(categoryId: String, depth: Int): Int {
        val children = getChildrenOnce(categoryId)
        if (children.isEmpty()) return depth
        return children.maxOf { walkDepth(it.id, depth + 1) }
    }

    @Query("SELECT * FROM categories WHERE parentId = :parentId")
    suspend fun getChildrenOnce(parentId: String): List<CategoryEntity>

    // Link relations

    @Query("""
        SELECT c.* FROM categories c
        INNER JOIN link_categories lc ON c.id = lc.categoryId
        WHERE lc.linkId = :linkId
    """)
    suspend fun getCategoryForLink(linkId: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assignLinkToCategory(linkCategory: LinkCategoryEntity)

    @Query("DELETE FROM link_categories WHERE linkId = :linkId")
    suspend fun removeLinkCategory(linkId: String)

    @Query("DELETE FROM link_categories WHERE categoryId = :categoryId")
    suspend fun removeLinksFromCategory(categoryId: String)

    @Query("""
        SELECT c.*, COUNT(lc.linkId) as linkCount
        FROM categories c
        LEFT JOIN link_categories lc ON c.id = lc.categoryId
        GROUP BY c.id
        ORDER BY c.name ASC
    """)
    fun getAllCategoriesWithCounts(): Flow<List<CategoryWithCount>>

    // Smart list counts

    @Query("SELECT COUNT(*) FROM links WHERE isOpened = 0")
    fun countUnopened(): Flow<Int>

    @Query("SELECT COUNT(*) FROM links WHERE isFavorite = 1")
    fun countFavorites(): Flow<Int>

    @Query("SELECT COUNT(*) FROM links WHERE isArchived = 1")
    fun countArchivedFlow(): Flow<Int>

    data class SmartListCounts(val unopened: Int, val favorites: Int, val archived: Int, val uncategorized: Int)

    @Query("""
        SELECT
            (SELECT COUNT(*) FROM links WHERE isOpened = 0 AND isArchived = 0) as unopened,
            (SELECT COUNT(*) FROM links WHERE isFavorite = 1 AND isArchived = 0) as favorites,
            (SELECT COUNT(*) FROM links WHERE isArchived = 1) as archived,
            (SELECT COUNT(*) FROM links l WHERE NOT EXISTS (SELECT 1 FROM link_categories lc WHERE lc.linkId = l.id)) as uncategorized
    """)
    fun getSmartListCounts(): Flow<SmartListCounts>

    @Query("""
        SELECT COUNT(*) FROM links l
        WHERE NOT EXISTS (SELECT 1 FROM link_categories lc WHERE lc.linkId = l.id)
    """)
    fun countUncategorized(): Flow<Int>
}

data class CategoryWithCount(
    val id: String,
    val name: String,
    val parentId: String?,
    val iconType: String,
    val createdAt: Long,
    val updatedAt: Long,
    val linkCount: Int
)
