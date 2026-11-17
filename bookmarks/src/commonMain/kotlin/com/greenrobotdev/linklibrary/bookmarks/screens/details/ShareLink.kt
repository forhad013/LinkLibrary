package com.greenrobotdev.linklibrary.bookmarks.screens.details

import androidx.compose.runtime.Composable

/** Returns a function that shares a link: the system share sheet on Android; elsewhere it copies the URL. */
@Composable
internal expect fun rememberShareLink(): (title: String, url: String) -> Unit
