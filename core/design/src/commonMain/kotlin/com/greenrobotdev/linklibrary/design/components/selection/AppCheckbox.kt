package com.greenrobotdev.linklibrary.design.components.selection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Checkbox component with Material 3 styling
 *
 * Selection control that allows users to select multiple options from a set.
 * Integrates with theme colors and supports proper accessibility.
 *
 * @param checked Whether the checkbox is currently checked
 * @param onCheckedChange Callback invoked when checkbox state changes
 * @param modifier Modifier to be applied to the checkbox
 * @param enabled Whether the checkbox is enabled for interaction
 * @param label Optional text label to display next to the checkbox
 */
@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null
) {
    if (label != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = androidx.compose.material3.CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedBorderColor = MaterialTheme.colorScheme.primary,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledCheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledUncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            )
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                )
            }
        }
    } else {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = androidx.compose.material3.CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                disabledCheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledUncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )
    }
}

/**
 * Checkbox with tri-state support (indeterminate state)
 *
 * @param state TriStateCheckboxState (Checked, Unchecked, or Indeterminate)
 * @param onClick Callback invoked when checkbox is clicked
 * @param modifier Modifier to be applied to the checkbox
 * @param enabled Whether the checkbox is enabled for interaction
 * @param label Optional text label to display next to the checkbox
 */
@Composable
fun AppCheckbox(
    state: TriStateCheckboxState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null
) {
    val checkedState = when (state) {
        is TriStateCheckboxState.Checked -> true
        is TriStateCheckboxState.Unchecked -> false
        is TriStateCheckboxState.Indeterminate -> false
    }

    AppCheckbox(
        checked = checkedState,
        onCheckedChange = { onClick() },
        modifier = modifier,
        enabled = enabled,
        label = label
    )
}

/**
 * Sealed class for tri-state checkbox states
 */
sealed class TriStateCheckboxState {
    object Checked : TriStateCheckboxState()
    object Unchecked : TriStateCheckboxState()
    object Indeterminate : TriStateCheckboxState()
}