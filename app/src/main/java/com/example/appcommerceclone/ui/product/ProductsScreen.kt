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
import androidx.compose.material.pullrefresh.PullRefreshState
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
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.ui.common.NoConnectionPlaceHolder
import com.example.appcommerceclone.ui.common.shimmerEffect
import com.example.appcommerceclone.util.productList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    loadingState: LoadingState,
    products: List<Product>,
    onProductClicked: (Product) -> Unit,
    onRefresh: () -> Unit
) {
    val isRefreshing: Boolean by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState: PullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    if (isConnected) {
        Box(modifier = modifier.pullRefresh(pullRefreshState)) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.padding_small),
                    alignment = Alignment.Top
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(id = R.dimen.padding_small),
                    alignment = Alignment.CenterHorizontally
                ),
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small))
            ) {
                if (loadingState == LoadingState.LOADING) {
                    items(12) {
                        ProductItemWithShimmer()
                    }
                } else {
                    items(products) { product: Product ->
                        ProductItem(
                            product = product,
                            onProductClicked = { onProductClicked(product) }
                        )
                    }
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(alignment = Alignment.TopCenter),
                refreshing = isRefreshing,
                state = pullRefreshState
            )
        }
    } else {
        NoConnectionPlaceHolder(modifier.fillMaxSize())
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
        modifier = modifier,
        onClick = onProductClicked,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_size_small)),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_small),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.item_product_title_height))
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                text = product.name,
                textAlign = TextAlign.Justify,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
            Text(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.item_product_subtitle_height))
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
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
fun ProductItemWithShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_size_small)),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.padding_small),
                alignment = Alignment.Top
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.item_product_image_height))
                    .fillMaxWidth()
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.item_product_title_height))
                    .fillMaxWidth()
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.item_product_subtitle_height))
                    .fillMaxWidth()
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductsScreen() {
    MaterialTheme {
        ProductsScreen(
            isConnected = true,
            loadingState = LoadingState.NOT_STARTED,
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
            isConnected = true,
            loadingState = LoadingState.LOADING,
            products = emptyList(),
            onProductClicked = {},
            onRefresh = {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewProductsScreenWhileNotConnected() {
    MaterialTheme {
        ProductsScreen(
            isConnected = false,
            loadingState = LoadingState.FAILURE,
            products = emptyList(),
            onProductClicked = {},
            onRefresh = {}
        )
    }
}