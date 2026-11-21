package com.greenrobotdev.lordofthelinks.bookmarks.motion

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.greenrobotdev.lordofthelinks.design.motion.LocalSharedTransitionScope

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

/**
 * Card → detail: the link's title on the Feed card and the big title on Link details share these
 * bounds ("title-<id>"). The text is scaled, not re-laid out, between the two sizes, so its line
 * breaks don't jump mid-transition. Only for real titles: a save without one shows its URL instead.
 */
@Composable
fun Modifier.sharedTitle(linkId: String): Modifier {
    val shared = LocalSharedTransitionScope.current ?: return this
    val animated = LocalNavAnimatedContentScope.current
    return with(shared) {
        this@sharedTitle.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "title-$linkId"),
            animatedVisibilityScope = animated,
            resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(ContentScale.FillWidth, Alignment.CenterStart),
        )
    }
}

/**
 * Library tile → list: a smart-list tile and the header of the list it opens share these bounds
 * ("scope-<key>"), so the tile grows into the header. Outside a shared-transition host it does nothing.
 */
@Composable
fun Modifier.sharedScope(scopeKey: String): Modifier {
    val shared = LocalSharedTransitionScope.current ?: return this
    val animated = LocalNavAnimatedContentScope.current
    return with(shared) {
        this@sharedScope.sharedBounds(
            sharedContentState = rememberSharedContentState(key = "scope-$scopeKey"),
            animatedVisibilityScope = animated,
        )
    }
}
