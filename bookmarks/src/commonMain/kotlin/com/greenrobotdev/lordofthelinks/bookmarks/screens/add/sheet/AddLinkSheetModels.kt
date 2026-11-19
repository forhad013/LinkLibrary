package com.greenrobotdev.lordofthelinks.bookmarks.screens.add.sheet

import com.greenrobotdev.lordofthelinks.bookmarks.domain.isValidLink

data class AddLinkSheetState(
    val url: String = "",
    val clipboardUrl: String? = null,
) {
    val isValid: Boolean get() = isValidLink(url)
    val showError: Boolean get() = url.isNotEmpty() && !isValid
    val showClipboardRow: Boolean get() = clipboardUrl != null && clipboardUrl != url
}

sealed interface AddLinkSheetEvent {
    data class UrlChanged(val url: String) : AddLinkSheetEvent
}
