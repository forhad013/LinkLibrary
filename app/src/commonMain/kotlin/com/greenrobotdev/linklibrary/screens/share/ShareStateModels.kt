package com.greenrobotdev.linklibrary.screens.share

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
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
    val error: String? = null
) {
    companion object {
        val Saver: Saver<ShareState, *> = listSaver(
            saver = Saver(
                save = { state ->
                    listOf(
                        state.linkId,
                        state.linkTitle,
                        state.linkUrl,
                        state.selectedPlatform,
                        state.customMessage,
                        state.isSharing,
                        state.error
                    )
                },
                restore = { list ->
                    ShareState(
                        linkId = list[0] as String,
                        linkTitle = list[1] as String,
                        linkUrl = list[2] as String,
                        selectedPlatform = list[3] as SharingPlatform?,
                        customMessage = list[4] as String,
                        isSharing = list[5] as Boolean,
                        error = list[6] as String?
                    )
                }
            )
        )
    }
}

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
