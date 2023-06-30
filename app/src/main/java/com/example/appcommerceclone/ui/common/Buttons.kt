package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R

/**
 * @param primaryActionContent is positioned on the right side of the screen
 * @param secondaryActionContent is positioned on the left side of the screen
 */
@Composable
fun DoubleActionButton(
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit,
    isPrimaryActionEnabled: Boolean = true,
    primaryActionContent: @Composable (RowScope.() -> Unit),
    onSecondaryAction: () -> Unit,
    isSecondaryActionEnabled: Boolean = true,
    secondaryActionContent: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SecondaryActionButton(
            modifier = Modifier.width(dimensionResource(id = R.dimen.btn_min_width_size)),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            onSecondaryAction = onSecondaryAction,
            isSecondaryActionEnabled = isSecondaryActionEnabled,
            secondaryActionContent = secondaryActionContent
        )
        PrimaryActionButton(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.padding_medium))
                .weight(1f),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_size_small)),
            onPrimaryAction = onPrimaryAction,
            isPrimaryActionEnabled = isPrimaryActionEnabled,
            primaryActionContent = primaryActionContent
        )
    }
}

@Composable
fun PrimaryActionButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    onPrimaryAction: () -> Unit,
    isPrimaryActionEnabled: Boolean = true,
    primaryActionContent: @Composable (RowScope.() -> Unit)
) {
    TextButton(
        modifier = modifier.heightIn(min = 48.dp),
        onClick = onPrimaryAction,
        enabled = isPrimaryActionEnabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.primaryColor),
            contentColor = colorResource(id = R.color.onPrimaryColor)
        ),
        content = primaryActionContent
    )
}

@Composable
fun SecondaryActionButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    onSecondaryAction: () -> Unit,
    isSecondaryActionEnabled: Boolean = true,
    secondaryActionContent: @Composable (RowScope.() -> Unit)
) {
    OutlinedButton(
        modifier = modifier.heightIn(min = 48.dp),
        onClick = onSecondaryAction,
        enabled = isSecondaryActionEnabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = colorResource(id = R.color.primaryColor)
        ),
        content = secondaryActionContent
    )
}