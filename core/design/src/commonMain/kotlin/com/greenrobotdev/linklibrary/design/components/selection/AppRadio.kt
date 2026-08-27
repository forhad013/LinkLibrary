package com.greenrobotdev.linklibrary.design.components.selection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacing
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Radio button component with Material 3 styling
 *
 * Selection control that allows users to select one option from a set.
 * Integrates with theme colors and supports proper accessibility.
 *
 * @param selected Whether this radio button is currently selected
 * @param onClick Callback invoked when radio button is clicked
 * @param modifier Modifier to be applied to the radio button
 * @param enabled Whether the radio button is enabled for interaction
 * @param label Text label to display next to the radio button
 */
@Composable
fun AppRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            colors = RadioButtonColors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledSelectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledUnselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )
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

/**
 * Radio button group for managing single selection from a list
 *
 * @param options List of string options to display
 * @param selectedOption Currently selected option
 * @param onOptionSelected Callback invoked when an option is selected
 * @param modifier Modifier to be applied to the radio button group
 * @param enabled Whether the radio buttons are enabled for interaction
 */
@Composable
fun AppRadioGroup(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier
    ) {
        options.forEach { option ->
            AppRadio(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                enabled = enabled,
                label = option
            )
        }
    }
}