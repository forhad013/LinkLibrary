package com.greenrobotdev.linklibrary.design.components.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AutoFetchButton(
    isFetching: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier, // Remove extra padding for better alignment
        enabled = enabled && !isFetching,
        shape = MaterialTheme.shapes.small
    ) {
        if (isFetching) CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 1.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        else Icon(
            Icons.Default.AutoAwesome,
            contentDescription = "Auto-fetch metadata",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Auto-fetch",
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
