package com.greenrobotdev.linklibrary.design.components.selection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacing
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Slider component with Material 3 styling
 *
 * Allows users to select a value from a continuous range. Integrates with
 * theme colors and supports value display.
 *
 * @param value Current slider value
 * @param onValueChange Callback invoked when value changes
 * @param modifier Modifier to be applied to the slider
 * @param enabled Whether the slider is enabled for interaction
 * @param valueRange Range of possible values (min to max)
 * @param steps Number of discrete steps (0 for continuous)
 * @param showValue Whether to display current value
 * @param valueLabel Optional custom label for value display
 */
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showValue: Boolean = false,
    valueLabel: String? = null
) {
    if (showValue || valueLabel != null) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                colors = SliderColors(
                    thumbColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    activeTrackColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    activeTickColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    inactiveTrackColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    inactiveTickColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledActiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledActiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    disabledInactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledInactiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            )

            val displayValue = valueLabel ?: value.toString()
            Text(
                text = displayValue,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
        }
    } else {
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
            colors = SliderColors(
                thumbColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                activeTrackColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                activeTickColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                inactiveTrackColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                inactiveTickColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledActiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledActiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledInactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledInactiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )
    }
}

/**
 * Range slider for selecting two values (min and max)
 *
 * @param values Current range values (min, max)
 * @param onValuesChange Callback invoked when range values change
 * @param modifier Modifier to be applied to the range slider
 * @param enabled Whether the range slider is enabled for interaction
 * @param valueRange Range of possible values (min to max)
 * @param steps Number of discrete steps (0 for continuous)
 * @param showValues Whether to display current values
 */
@Composable
fun AppRangeSlider(
    values: ClosedFloatingPointRange<Float>,
    onValuesChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showValues: Boolean = false
) {
    var rangeStart by remember(values) { mutableFloatStateOf(values.start) }
    var rangeEnd by remember(values) { mutableFloatStateOf(values.endInclusive) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.RangeSlider(
            value = rangeStart..rangeEnd,
            onValueChange = { newRange ->
                rangeStart = newRange.start
                rangeEnd = newRange.endInclusive
                onValuesChange(newRange)
            },
            modifier = Modifier.weight(1f),
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
            colors = SliderColors(
                thumbColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                activeTrackColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                activeTickColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                inactiveTrackColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                inactiveTickColor = if (enabled) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledActiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledActiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledInactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledInactiveTickColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        )

        if (showValues) {
            Text(
                text = "${rangeStart.toString()} - ${rangeEnd.toString()}",
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
}