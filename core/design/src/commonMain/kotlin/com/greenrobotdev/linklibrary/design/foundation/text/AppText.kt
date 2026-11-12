package com.greenrobotdev.linklibrary.design.foundation.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Foundation text component with automatic theme integration
 *
 * This wrapper provides a simplified text component that automatically integrates
 * with the Material 3 theme while offering sensible defaults for common use cases.
 *
 * @param text The text to display
 * @param modifier Modifier to be applied to the text
 * @param style TextStyle to apply. Defaults to LocalTextStyle
 * @param color Color to apply to the text. Defaults to theme's onSurface color
 * @param textAlign Text alignment
 * @param overflow How visual overflow should be handled
 * @param maxLines Maximum number of lines
 * @param onTextLayout Callback invoked when text layout is computed
 */
@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val effectiveColor = if (color == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurface
    } else {
        color
    }

    Text(
        text = text,
        modifier = modifier,
        style = style,
        color = effectiveColor,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = onTextLayout
    )
}