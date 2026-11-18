package com.greenrobotdev.linklibrary.bookmarks.motion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.greenrobotdev.linklibrary.design.motion.LocalSharedTransitionScope

/**
 * Card → detail: a link's type thumbnail in the Feed and the big type badge on Link details share
 * these bounds, so the thumbnail grows into the badge (and shrinks back on Back). Key: "thumb-<id>".
 * Put it before size/clip/background. Outside a shared-transition host it does nothing.
 */
@Composable
fun Modifier.sharedThumb(linkId: String): Modifier {
    val shared = LocalSharedTransitionScope.current ?: return this
    val animated = LocalNavAnimatedContentScope.current
    return with(shared) {
        this@sharedThumb.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "thumb-$linkId"),
            animatedVisibilityScope = animated,
        )
    }
}
