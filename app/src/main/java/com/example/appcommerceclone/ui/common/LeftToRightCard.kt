package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.appcommerceclone.R

@Composable
fun LeftToRightCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
        shape = RoundedCornerShape(
            topEnd = dimensionResource(id = R.dimen.corner_size_small),
            bottomEnd = dimensionResource(id = R.dimen.corner_size_small)
        ),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.stroke_size_small),
            color = colorResource(id = R.color.stroke_color_dark)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LeftToRightCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
        shape = RoundedCornerShape(
            topEnd = dimensionResource(id = R.dimen.corner_size_small),
            bottomEnd = dimensionResource(id = R.dimen.corner_size_small)
        ),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.stroke_size_small),
            color = colorResource(id = R.color.stroke_color_dark)
        ),
        content = content
    )
}