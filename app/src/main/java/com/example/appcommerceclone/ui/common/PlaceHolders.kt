package com.example.appcommerceclone.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R

@Composable
fun NoConnectionPlaceHolder(modifier: Modifier = Modifier) {
    PlaceHolder(
        modifier = modifier,
        placeHolderImage = painterResource(id = R.drawable.ic_no_connection),
        placeHolderImageContentDescription = stringResource(id = R.string.content_desc_no_internet_connection_image),
        placeHolderText = stringResource(id = R.string.error_no_connection)
    )
}

@Composable
fun PlaceHolder(
    modifier: Modifier = Modifier,
    placeHolderImage: Painter? = null,
    placeHolderImageContentDescription: String? = null,
    placeHolderText: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        placeHolderImage?.let {
            Image(
                modifier = Modifier.size(200.dp),
                painter = it,
                contentDescription = placeHolderImageContentDescription
            )
        }
        Text(
            text = placeHolderText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewNoConnectionPlaceHolder() {
    MaterialTheme {
        NoConnectionPlaceHolder()
    }
}