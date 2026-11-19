package com.greenrobotdev.lordofthelinks.bookmarks.screens.categoryPicker

import com.greenrobotdev.lordofthelinks.bookmarks.model.Category

/** One visible row of the browse tree or the search results. */
data class TreeRow(
    val category: Category,
    val depth: Int, // 0 = root
    val hasChildren: Boolean,
    val breadcrumb: String,
)

/** Flatten the category tree depth-first; children appear only under an expanded parent. */
fun flattenTree(categories: List<Category>, expandedIds: Set<String>): List<TreeRow> {
    val byParent = categories.groupBy { it.parentId }
    fun emit(parentId: String?, depth: Int, out: MutableList<TreeRow>) {
        (byParent[parentId] ?: emptyList()).sortedBy { it.name.lowercase() }.forEach { category ->
            val children = byParent[category.id].orEmpty()
            out.add(TreeRow(category, depth, children.isNotEmpty(), category.path(categories)))
            if (children.isNotEmpty() && category.id in expandedIds) emit(category.id, depth + 1, out)
        }
    }
    return buildList { emit(null, 0, this) }
}

/** Categories whose own name matches the query (case-insensitive), each with its breadcrumb. */
fun searchRows(categories: List<Category>, query: String): List<TreeRow> =
    categories.filter { it.name.contains(query.trim(), ignoreCase = true) }
        .sortedBy { it.name.lowercase() }
        .map { TreeRow(it, 0, false, it.path(categories)) }

/** True when a "Create \"query\"" row should be offered: no category owns that exact name. */
fun needsCreateRow(categories: List<Category>, query: String): Boolean =
    query.isNotBlank() && categories.none { it.name.equals(query.trim(), ignoreCase = true) }

/** Ancestor ids from root down to the category's parent (empty for roots). */
fun ancestorsOf(categoryId: String, categories: List<Category>): List<String> {
    val byId = categories.associateBy { it.id }
    val chain = mutableListOf<String>()
    var current = byId[categoryId]?.parentId
    while (current != null) {
        chain.add(0, current)
        current = byId[current]?.parentId
    }
    return chain
}

/** The category and every descendant id (delete-all scope; includes the root id even if missing). */
fun subtreeIds(categoryId: String, categories: List<Category>): Set<String> {
    val byParent = categories.groupBy { it.parentId }
    val out = mutableSetOf(categoryId)
    var frontier = byParent[categoryId].orEmpty().map { it.id }
    while (frontier.isNotEmpty()) {
        out += frontier
        frontier = frontier.flatMap { byParent[it].orEmpty().map { c -> c.id } }
    }
    return out
}

/** Depth of a category (root = 1); null/missing id → 0. */
fun depthOf(categoryId: String?, categories: List<Category>): Int {
    var depth = 0
    var current = categoryId?.let { id -> categories.firstOrNull { it.id == id } }
    while (current != null) {
        depth++
        current = current.parentId?.let { id -> categories.firstOrNull { it.id == id } }
    }
    return depth
}

const val MAX_CATEGORY_DEPTH = 5

/** A child may be added under categoryId only while the subtree stays within the depth limit. */
fun canAddChildUnder(categoryId: String?, categories: List<Category>): Boolean =
    depthOf(categoryId, categories) < MAX_CATEGORY_DEPTH

/** null when the name is valid; otherwise a user-facing error. Sibling names are unique, case-insensitive. */
fun validateCategoryName(
    name: String,
    parentId: String?,
    categories: List<Category>,
    excludeId: String? = null,
): String? {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return "Name can't be empty"
    val siblingTaken = categories.any {
        it.id != excludeId && it.parentId == parentId && it.name.equals(trimmed, ignoreCase = true)
    }
    if (siblingTaken) return "A category with this name already exists here"
    return null
}
