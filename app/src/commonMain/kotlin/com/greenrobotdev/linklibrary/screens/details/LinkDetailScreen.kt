package com.greenrobotdev.linklibrary.screens.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

@Composable
fun LinkDetailScreen(
    routeKey: NavKey,
    linkId: String,
    onBack: () -> Unit
) {
    // TODO: Implement LinkDetail screen
    Text("Link Detail: $linkId")
}
