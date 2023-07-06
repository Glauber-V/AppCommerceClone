package com.example.appcommerceclone.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy.*
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.model.product.Product
import com.example.appcommerceclone.ui.common.shimmerEffect
import com.example.appcommerceclone.util.productList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    products: List<Product>,
    onProductClicked: (Product) -> Unit,
    onRefresh: () -> Unit
) {
    val isRefreshing by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        if (isLoading) {
            ProductsScreenContentWithShimmer(modifier = Modifier.fillMaxSize())
        } else {
            ProductsScreenContent(
                modifier = Modifier.fillMaxSize(),
                products = products,
                onProductClicked = onProductClicked
            )
        }
        PullRefreshIndicator(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState
        )
    }
}

@Composable
fun ProductsScreenContent(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onProductClicked: (Product) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(products) { product: Product ->
            ProductItem(
                product = product,
                onProductClicked = { onProductClicked(product) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClicked: () -> Unit
) {
    Card(
        onClick = onProductClicked,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_size_small)),
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val paddingValues = PaddingValues(
                top = dimensionResource(id = R.dimen.padding_small),
                start = dimensionResource(id = R.dimen.padding_medium),
                end = dimensionResource(id = R.dimen.padding_medium)
            )
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.item_product_image_height)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.ic_broken_image)
                    .build(),
                contentDescription = null
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.item_product_title_height))
                    .padding(paddingValues),
                text = product.name,
                textAlign = TextAlign.Justify,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
            Text(
                modifier = modifier
                    .height(dimensionResource(id = R.dimen.item_product_subtitle_height))
                    .padding(paddingValues),
                text = product.getFormattedPrice(),
                textAlign = TextAlign.Justify,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@Composable
fun ProductsScreenContentWithShimmer(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(12) {
            ProductItemWithShimmer()
        }
    }
}

@Composable
fun ProductItemWithShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val paddingValues = PaddingValues(
            top = dimensionResource(id = R.dimen.padding_small),
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_medium)
        )
        Box(
            modifier = modifier
                .height(dimensionResource(id = R.dimen.item_product_image_height))
                .fillMaxWidth()
                .padding(paddingValues)
                .shimmerEffect()
        )
        Box(
            modifier = modifier
                .height(dimensionResource(id = R.dimen.item_product_title_height))
                .fillMaxWidth()
                .padding(paddingValues)
                .shimmerEffect()
        )
        Box(
            modifier = modifier
                .height(dimensionResource(id = R.dimen.item_product_subtitle_height))
                .fillMaxWidth()
                .padding(paddingValues)
                .shimmerEffect()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductsScreen() {
    MaterialTheme {
        ProductsScreen(
            isLoading = false,
            products = productList,
            onProductClicked = {},
            onRefresh = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductsScreenWhileLoading() {
    MaterialTheme {
        ProductsScreen(
            isLoading = true,
            products = emptyList(),
            onProductClicked = {},
            onRefresh = {}
        )
    }
}