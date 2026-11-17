package com.greenrobotdev.linklibrary.screens.library.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Library Tab Navigation Routes
 * Manages all screens within the Library tab
 */
sealed class LibraryNavRoutes : NavKey {
    // Base library screen
    data object LibraryBase : LibraryNavRoutes()

    // Link detail and editing
    data class LinkDetail(val linkId: String) : LibraryNavRoutes()
    data class AddLink(val initialUrl: String? = null) : LibraryNavRoutes()
    data class NoteEditor(val noteId: String? = null) : LibraryNavRoutes()

    // Categories and tags management
    data object Categories : LibraryNavRoutes()
    data object Tags : LibraryNavRoutes()
    data object AddCategory : LibraryNavRoutes()
    data object AddTag : LibraryNavRoutes()
}