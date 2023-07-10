package com.example.appcommerceclone.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.LeftToRightCard

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onProductCategorySelected: (ProductCategories) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_medium),
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.Start
    ) {
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_jewelery),
            onProductCategorySelected = {
                onProductCategorySelected(ProductCategories.JEWELERY)
            }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_electronics),
            onProductCategorySelected = {
                onProductCategorySelected(ProductCategories.ELECTRONICS)
            }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_mens_clothing),
            onProductCategorySelected = {
                onProductCategorySelected(ProductCategories.MENS_CLOTHING)
            }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_women_s_clothing),
            onProductCategorySelected = {
                onProductCategorySelected(ProductCategories.WOMENS_CLOTHING)
            }
        )
    }
}

@Composable
fun ProductCategoryItem(
    modifier: Modifier = Modifier,
    name: String,
    onProductCategorySelected: () -> Unit
) {
    LeftToRightCard(
        modifier = modifier
            .height(65.dp)
            .fillMaxWidth()
            .padding(end = dimensionResource(id = R.dimen.padding_large)),
        onClick = onProductCategorySelected
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(start = 40.dp),
                text = name,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewCategoriesScreen() {
    MaterialTheme {
        CategoriesScreen(onProductCategorySelected = {})
    }
}