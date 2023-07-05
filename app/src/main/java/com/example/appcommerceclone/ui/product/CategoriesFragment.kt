package com.example.appcommerceclone.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcommerceclone.R
import com.example.appcommerceclone.ui.common.LeftToRightCard
import com.example.appcommerceclone.viewmodels.ProductCategories

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onProductCategorySelected: (ProductCategories) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_jewelery),
            productCategory = ProductCategories.JEWELERY,
            onProductCategorySelected = { onProductCategorySelected(it) }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_electronics),
            productCategory = ProductCategories.ELECTRONICS,
            onProductCategorySelected = { onProductCategorySelected(it) }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_mens_clothing),
            productCategory = ProductCategories.MENS_CLOTHING,
            onProductCategorySelected = { onProductCategorySelected(it) }
        )
        ProductCategoryItem(
            name = stringResource(id = R.string.category_name_women_s_clothing),
            productCategory = ProductCategories.WOMENS_CLOTHING,
            onProductCategorySelected = { onProductCategorySelected(it) }
        )
    }
}

@Composable
fun ProductCategoryItem(
    modifier: Modifier = Modifier,
    name: String,
    productCategory: ProductCategories,
    onProductCategorySelected: (ProductCategories) -> Unit
) {
    LeftToRightCard(
        onClick = { onProductCategorySelected(productCategory) },
        modifier = modifier.height(65.dp)
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
        CategoriesScreen {}
    }
}