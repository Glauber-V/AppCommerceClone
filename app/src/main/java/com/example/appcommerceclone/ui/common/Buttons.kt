package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.appcommerceclone.R

/**
 * @param primaryContent is positioned on the right side of the screen
 * @param secondaryContent is positioned on the left side of the screen
 */
@Composable
fun DoubleActionButton(
    modifier: Modifier = Modifier,
    primaryActionEnabled: Boolean = true,
    onPrimaryAction: () -> Unit,
    primaryButtonColors: ButtonColors = ButtonDefaults.textButtonColors(),
    primaryContent: @Composable RowScope.() -> Unit,
    secondaryActionEnabled: Boolean = true,
    onSecondaryAction: () -> Unit,
    secondaryButtonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    secondaryContent: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.height(dimensionResource(id = R.dimen.btn_min_height_size)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onSecondaryAction,
            enabled = secondaryActionEnabled,
            modifier = Modifier.width(dimensionResource(id = R.dimen.btn_min_width_size)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            colors = secondaryButtonColors,
            content = secondaryContent
        )
        TextButton(
            onClick = onPrimaryAction,
            enabled = primaryActionEnabled,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
                .weight(1f),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            colors = primaryButtonColors,
            content = primaryContent
        )
    }
}