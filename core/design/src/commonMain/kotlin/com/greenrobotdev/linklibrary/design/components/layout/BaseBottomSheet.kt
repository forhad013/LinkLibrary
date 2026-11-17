package com.greenrobotdev.linklibrary.design.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.greenrobotdev.linklibrary.design.foundation.icon.AppIcon

/**
 * Modal bottom sheet with the app's standard scaffold: title + round tonal close button,
 * fixed window-fraction height, ime/nav padding, 16dp content padding.
 *
 * Fixed height from the window, not fillMaxHeight(): the sheet's constraints shrink with the
 * keyboard, and a height that follows them feeds back into imePadding, so the sheet bounces.
 *
 * @param heightFraction sheet height as a fraction of the window (default 0.92)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheet(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    heightFraction: Float = 0.92f,
    closeContentDescription: String = "Close",
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetHeight = with(LocalDensity.current) {
        (LocalWindowInfo.current.containerSize.height * heightFraction).toDp()
    }

    ModalBottomSheet(
        modifier = modifier.statusBarsPadding(),
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { true }),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight) // scrim stays visible above; see KDoc for why not fillMaxHeight
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(onClick = onClose),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = closeContentDescription,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            content()
        }
    }
}
