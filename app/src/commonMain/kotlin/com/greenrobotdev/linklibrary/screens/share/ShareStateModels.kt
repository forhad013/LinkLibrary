package com.greenrobotdev.linklibrary.screens.share

import kotlinx.serialization.Serializable

/**
 * State for Share Pop-up screen
 */
@Serializable
data class ShareState(
    val linkId: String = "",
    val linkTitle: String = "",
    val linkUrl: String = "",
    val selectedPlatform: SharingPlatform? = null,
    val customMessage: String = "",
    val isSharing: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Social media platforms for sharing
 */
@Serializable
sealed class SharingPlatform {
    @Serializable
    data object Twitter : SharingPlatform()

    @Serializable
    data object Facebook : SharingPlatform()

    @Serializable
    data object LinkedIn : SharingPlatform()

    @Serializable
    data object WhatsApp : SharingPlatform()

    @Serializable
    data object Reddit : SharingPlatform()

    @Serializable
    data object Email : SharingPlatform()

    @Serializable
    data object CopyLink : SharingPlatform()

    @Serializable
    data object More : SharingPlatform()

    val displayName: String
        get() = when (this) {
            is Twitter -> "Twitter"
            is Facebook -> "Facebook"
            is LinkedIn -> "LinkedIn"
            is WhatsApp -> "WhatsApp"
            is Reddit -> "Reddit"
            is Email -> "Email"
            is CopyLink -> "Copy Link"
            is More -> "More"
        }

    val iconName: String
        get() = when (this) {
            is Twitter -> "twitter"
            is Facebook -> "facebook"
            is LinkedIn -> "linkedin"
            is WhatsApp -> "whatsapp"
            is Reddit -> "reddit"
            is Email -> "email"
            is CopyLink -> "content_copy"
            is More -> "more_horiz"
        }
}

/**
 * Share Events
 */
sealed interface ShareEvent {
    object Initialize : ShareEvent
    data class SelectPlatform(val platform: SharingPlatform) : ShareEvent
    data class UpdateMessage(val message: String) : ShareEvent
    object Share : ShareEvent
    object ClearError : ShareEvent
    object Dismiss : ShareEvent
}
