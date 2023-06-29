package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R

/**
 * @param primaryContent is positioned on the right side of the screen
 * @param secondaryContent is positioned on the left side of the screen
 */
@Composable
fun DoubleActionButton(
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit,
    isPrimaryActionEnabled: Boolean = true,
    primaryContent: @Composable (RowScope.() -> Unit),
    onSecondaryAction: () -> Unit,
    isSecondaryActionEnabled: Boolean = true,
    secondaryContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SecondaryActionButton(
            modifier = Modifier.width(dimensionResource(id = R.dimen.btn_min_width_size)),
            onSecondaryAction = onSecondaryAction,
            isSecondaryActionEnabled = isSecondaryActionEnabled,
            secondaryContent = secondaryContent
        )
        PrimaryActionButton(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
                .weight(1f),
            onPrimaryAction = onPrimaryAction,
            isPrimaryActionEnabled = isPrimaryActionEnabled,
            primaryContent = primaryContent
        )
    }
}

@Composable
fun PrimaryActionButton(
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit,
    isPrimaryActionEnabled: Boolean,
    primaryContent: @Composable (RowScope.() -> Unit)
) {
    TextButton(
        modifier = modifier.heightIn(min = 48.dp),
        onClick = onPrimaryAction,
        enabled = isPrimaryActionEnabled,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.primaryColor),
            contentColor = colorResource(id = R.color.white_100)
        ),
        content = primaryContent
    )
}

@Composable
fun SecondaryActionButton(
    modifier: Modifier = Modifier,
    onSecondaryAction: () -> Unit,
    isSecondaryActionEnabled: Boolean,
    secondaryContent: @Composable (RowScope.() -> Unit)
) {
    OutlinedButton(
        modifier = modifier.heightIn(min = 48.dp),
        onClick = onSecondaryAction,
        enabled = isSecondaryActionEnabled,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = colorResource(id = R.color.primaryColor)
        ),
        content = secondaryContent
    )
}